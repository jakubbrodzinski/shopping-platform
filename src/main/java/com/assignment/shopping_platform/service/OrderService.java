package com.assignment.shopping_platform.service;

import com.assignment.shopping_platform.dto.OrderCreateDto;
import com.assignment.shopping_platform.dto.OrderDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.factory.OrderFactory;
import com.assignment.shopping_platform.repositroy.OrderRepository;
import com.assignment.shopping_platform.repositroy.ProductRepository;
import com.assignment.shopping_platform.repositroy.model.Order;
import com.assignment.shopping_platform.repositroy.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static org.springframework.data.domain.Sort.Order.desc;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {
    private static final Sort SORT_ORDER = Sort.by(desc("createdAt"));

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final TotalsCalculator totalsCalculator;

    public OrderDto placeOrder(OrderCreateDto orderCreateDto) {
        var products = findProducts(extractProductIds(orderCreateDto));
        var order = orderFactory.create(orderCreateDto, products);
        orderRepository.save(order);
        return OrderDto.from(order, totalsCalculator.calculateTotals(order));
    }

    private Set<UUID> extractProductIds(OrderCreateDto orderCreateDto) {
        return orderCreateDto.items().stream()
                .map(OrderCreateDto.OrderItemDto::productId)
                .map(UUID::fromString)
                .collect(toSet());
    }

    private List<Product> findProducts(Set<UUID> productIds) {
        var result = productRepository.findByExternalIdIn(productIds);
        if (productIds.size() != result.size()) {
            throw new ProductNotFoundException();
        } else {
            return result;
        }
    }

    public List<OrderDto> queryByCreatedAtTimestamp(Instant from, Instant to, int pageNumber, int pageSize) {
        var orderIds = orderRepository.findByCreatedAtBetween(from, to, pageRequest(pageNumber, pageSize))
                .map(Order::getId)
                .get().collect(toSet());
        return orderRepository.fetchOrdersWithAssociations(orderIds).stream()
                .map(order -> OrderDto.from(order, totalsCalculator.calculateTotals(order)))
                .toList();
    }

    private static PageRequest pageRequest(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize, SORT_ORDER);
    }
}

