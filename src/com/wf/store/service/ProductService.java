package com.wf.store.service;

import com.wf.store.domain.Basket;
import com.wf.store.domain.Order;
import com.wf.store.domain.Product;
import com.wf.store.exception.EntityWasNotFoundException;
import com.wf.store.exception.NotEnoughProductsInWarehouseException;
import com.wf.store.repository.BasketRepository;
import com.wf.store.repository.ProductRepository;
import com.wf.store.warehouse.task.CheckProductAvailabilityTask;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final CheckProductAvailabilityTask productAvailabilityTask;
    private final OrderService orderService;
    private final BasketRepository basketRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CheckProductAvailabilityTask productAvailabilityTask,
                          OrderService orderService, BasketRepository basketRepository) {
        this.productRepository = productRepository;
        this.productAvailabilityTask = productAvailabilityTask;
        this.orderService = orderService;
        this.basketRepository = basketRepository;
    }

    public Boolean addProductToCart(long productId, long orderId, int amount)
            throws EntityWasNotFoundException, NotEnoughProductsInWarehouseException {
        log.debug("Add product to cart method has been started ...");
        Product storeProduct = retrieveProductById(productId);
        long warehouseId = storeProduct.getWarehouseId();
        productAvailabilityTask.setWarehouseProductId(warehouseId);
        productAvailabilityTask.setNeededAmount(amount);
        @Cleanup("shutdown") ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(productAvailabilityTask);
        addProductToOrder(orderId, amount, storeProduct, future, storeProduct);
        return true;
    }

    /**
     * I understand, that logic of this method looks strange,
     * because we are gathering different information that could be or
     * could not be used. Everything depends on response from future.
     * I decided to use this behavior just to show the purpose of using future
     * for this kind of async job.
     *
     */
    private void addProductToOrder(long orderId, int amount, Product storeProduct,
                                   Future<Boolean> productsWarehouseAvailabilityFuture, Product product)
            throws EntityWasNotFoundException, NotEnoughProductsInWarehouseException {

        Order order = orderService.retrieveOrderById(orderId);
        Basket basket = getProductBasket(amount, storeProduct, product, order);
        if (isProductAvailableInWarehouse(productsWarehouseAvailabilityFuture)){
            basketRepository.save(basket);
        }
    }

    private boolean isProductAvailableInWarehouse(Future<Boolean> productsWarehouseAvailabilityFuture) throws NotEnoughProductsInWarehouseException, EntityWasNotFoundException {
        try {
            if (productsWarehouseAvailabilityFuture.get()) {
              return true;
            } else {
                throw new NotEnoughProductsInWarehouseException("There are not enough products in warehouse");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new EntityWasNotFoundException("Could not reach warehouse micro service");
        }
    }

    private Basket getProductBasket(int amount, Product storeProduct, Product product, Order order) {
        Basket basket = basketRepository.getBasketByOrderAndProduct(order, product);
        if (basket != null) {
            log.debug("Using existing basket");
            basket.setAmount(basket.getAmount() + amount);
        } else {
            log.debug("Creating new basket");
            basket = new Basket();
            basket.setOrder(order);
            basket.setProduct(storeProduct);
            basket.setAmount(amount);
        }
        return basket;
    }

    private Product retrieveProductById(long productId) throws EntityWasNotFoundException {
        Optional<Product> productOptional = productRepository.findById(productId);
        productOptional.orElseThrow(() -> new EntityWasNotFoundException("Could not find product with id = " + productId));
        log.debug("The product has been retrieved");
        return productOptional.get();
    }
}