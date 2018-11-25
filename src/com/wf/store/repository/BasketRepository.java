package com.wf.store.repository;

import com.wf.store.domain.Basket;
import com.wf.store.domain.Order;
import com.wf.store.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Long> {
    Basket getBasketByOrderAndProduct(Order order, Product product);
}
