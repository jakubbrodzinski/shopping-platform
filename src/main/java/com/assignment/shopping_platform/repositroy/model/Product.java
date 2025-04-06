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

    @Column(unique = true, updatable = false)
    private UUID externalId;

    private String name;

    private String description;

    private CurrencyUnit currency;

    private BigDecimal price;
}
