package com.wf.store.service;

import com.wf.store.domain.Customer;
import com.wf.store.exceptions.EntityWasNotFoundException;
import com.wf.store.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer retrieveCustomerById(long id) throws EntityWasNotFoundException {
        Optional<Customer> c = customerRepository.findById(id);
        if (c.isPresent()){
            return c.get();
        } else {
            throw new EntityWasNotFoundException("Could not find customer with id = " + id);
        }
    }
}
