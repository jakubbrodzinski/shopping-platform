package com.assignment.shopping_platform.utils;

import com.assignment.shopping_platform.repositroy.OrderRepository;
import com.assignment.shopping_platform.repositroy.ProductRepository;
import com.assignment.shopping_platform.repositroy.model.Order;
import com.assignment.shopping_platform.repositroy.model.OrderItem;
import com.assignment.shopping_platform.repositroy.model.Product;
import jakarta.transaction.Transactional;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@Transactional
public class OrderMotherObject {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Instant createdAt, Product product1, Money price1) {
        var order = order(createdAt);
        order.setItems(List.of(orderItem(product1, order, price1)));
        return orderRepository.save(order);
    }

    public Order createOrder(Instant createdAt, Product product1, Money price1, Product product2, Money price2) {
        var order = order(createdAt);
        order.setItems(List.of(orderItem(product1, order, price1), orderItem(product2, order, price2)));
        return orderRepository.save(order);
    }

    private Order order(Instant createdAt) {
        var order = new Order();
        order.setExternalId(UUID.randomUUID());
        order.setEmail("dummy@mail.com");
        order.setCreatedAt(createdAt);
        return order;
    }

    private OrderItem orderItem(Product product, Order order, Money price) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(price.getAmount());
        orderItem.setCurrency(price.getCurrencyUnit());
        return orderItem;
    }
}
