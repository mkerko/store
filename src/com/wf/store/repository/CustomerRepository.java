package com.wf.store.repository;

import com.wf.store.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findCustomerBySurname(String surname);
}
