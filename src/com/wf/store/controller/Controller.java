package com.wf.store.controller;

import com.wf.store.exception.EntityWasNotFoundException;
import com.wf.store.exception.NotEnoughProductsInWarehouseException;
import com.wf.store.service.CustomerService;
import com.wf.store.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@Log4j2
public class Controller {

    private final CustomerService customerService;

    private final ProductService productService;

    @Autowired
    public Controller(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping("/customerCheck")
    public String checkCustomer(@RequestParam(name = "customerId") Long productId) throws EntityWasNotFoundException {
        log.debug("customerCheck has been called");
        return customerService.retrieveCustomerById(productId).getName();
    }

    @GetMapping("/addProductToCart")
    public String addProductToCart(@NotNull @RequestParam(name = "productId") Long productId,
                                   @NotNull @RequestParam(name = "orderId") Long orderId,
                                   @NotNull @RequestParam(name = "amount") int amount)
            throws NotEnoughProductsInWarehouseException, EntityWasNotFoundException {
        log.debug("addProductToCart has been called");
        return productService.addProductToCart(productId, orderId, amount).toString();
    }
}
