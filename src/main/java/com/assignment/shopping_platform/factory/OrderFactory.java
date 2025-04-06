package com.assignment.shopping_platform.factory;

import com.assignment.shopping_platform.dto.OrderCreateDto;
import com.assignment.shopping_platform.repositroy.model.Order;
import com.assignment.shopping_platform.repositroy.model.OrderItem;
import com.assignment.shopping_platform.repositroy.model.Product;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.IntStream.range;

public class OrderFactory {
    public Order create(OrderCreateDto orderCreateDto, List<Product> lineItems) {
        var order = new Order();
        order.setExternalId(UUID.randomUUID());
        order.setEmail(orderCreateDto.email());
        order.setItems(lineItems(orderCreateDto.items(), groupByExternalId(lineItems), order));
        order.setCreatedAt(Instant.now());
        return order;
    }

    private List<OrderItem> lineItems(List<OrderCreateDto.OrderItemDto> items, Map<String, Product> productsByExternalId, Order order) {
        return items.stream()
                .<OrderItem>mapMulti((orderItemDto, orderItemConsumer) -> {
                    var product = productsByExternalId.get(orderItemDto.productId());
                    var orderItem = orderItem(product, order);
                    range(0, orderItemDto.quantity()).forEach(__ -> orderItemConsumer.accept(orderItem));
                })
                .toList();
    }

    private OrderItem orderItem(Product product, Order order) {
        if (product == null){
            throw  new IllegalArgumentException();
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(product.getPrice());
        orderItem.setCurrency(product.getCurrency());
        return orderItem;
    }

    private Map<String, Product> groupByExternalId(List<Product> lineItems) {
        return lineItems.stream().collect(Collectors.toMap(product -> product.getExternalId().toString(), identity()));
    }
}

