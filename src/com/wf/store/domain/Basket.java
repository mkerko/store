package com.wf.store.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "baskets")
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;

    @Column(name = "amount")
    private int amount;

}
