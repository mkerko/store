package com.wf.store;

import com.wf.store.exceptions.EntityWasNotFoundException;
import com.wf.store.service.CustomerService;
import com.wf.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final CustomerService customerService;

    private final ProductService productService;

    @Autowired
    public Controller(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping("/customerCheck")
    public String checkCustomer(@RequestParam(name = "customerId") Long productId){
        try {
            return customerService.retrieveCustomerById(productId).getName();
        } catch (EntityWasNotFoundException e) {
            return "Customer was not found.";
        }
    }

    @GetMapping("/productCheck")
    public Boolean checkProduct(@RequestParam(name = "productId") Long productId){
        try {
            return productService.checkProductAvailability(productId);
        } catch (EntityWasNotFoundException e) {
            return false;
        }
    }
}
