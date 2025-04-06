package com.assignment.shopping_platform.repositroy.model;

import jakarta.persistence.*;
import lombok.Data;
import org.joda.money.CurrencyUnit;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity(name = "`ORDER`")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CurrencyUnit currencyUnit;

    @Column(unique = true, updatable = false)
    private UUID externalId;

    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;
}
