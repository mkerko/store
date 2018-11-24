package com.wf.store.service;

import com.wf.store.domain.Product;
import com.wf.store.exceptions.EntityWasNotFoundException;
import com.wf.store.repository.ProductRepository;
import com.wf.store.warehouse.tasks.CheckProductAvailabilityTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final CheckProductAvailabilityTask productAvailabilityTask;

    @Autowired
    public ProductService(ProductRepository productRepository, CheckProductAvailabilityTask productAvailabilityTask) {
        this.productRepository = productRepository;
        this.productAvailabilityTask = productAvailabilityTask;
    }

    public Boolean checkProductAvailability(long productId) throws EntityWasNotFoundException {
        Optional<Product> p = productRepository.findById(productId);
        Product storeProduct;
        if (p.isPresent()){
            storeProduct = p.get();
        } else {
            throw new EntityWasNotFoundException("Could not find product with id = " + productId);
        }
        ExecutorService executorService = null;
        boolean isAvailable;

        try {
        long warehouseId = storeProduct.getWarehouseId();
        productAvailabilityTask.setWarehouseProductId(warehouseId);
        executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(productAvailabilityTask);
        isAvailable = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new EntityWasNotFoundException("Could not find product in warehouse");
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }

        return isAvailable;
    }
}
