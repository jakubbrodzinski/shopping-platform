package com.assignment.shopping_platform.repositroy.model;

import jakarta.persistence.*;
import lombok.Data;
import org.joda.money.CurrencyUnit;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "PRODUCT")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false,nullable = false)
    private UUID externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private CurrencyUnit currency;

    @Column(nullable = false)
    private BigDecimal price;
}
