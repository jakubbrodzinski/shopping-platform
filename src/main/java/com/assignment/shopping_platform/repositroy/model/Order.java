package com.assignment.shopping_platform.repositroy.model;

import jakarta.persistence.*;
import lombok.Data;
import org.joda.money.CurrencyUnit;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity(name = "ORDER_ENTITY")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private UUID externalId;

    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderItem> items;
}
