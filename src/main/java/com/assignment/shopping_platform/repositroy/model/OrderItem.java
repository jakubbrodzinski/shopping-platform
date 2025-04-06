package com.assignment.shopping_platform.repositroy.model;

import jakarta.persistence.*;
import lombok.Data;
import org.joda.money.CurrencyUnit;

import java.math.BigDecimal;

@Entity(name = "ORDER_ITEM")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @Column(nullable = false)
    private CurrencyUnit currency;

    @Column(nullable = false)
    private BigDecimal price;
}
