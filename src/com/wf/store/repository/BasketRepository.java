package com.wf.store.repository;

import com.wf.store.domain.Basket;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<Basket, Long> {
}
