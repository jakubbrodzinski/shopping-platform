package com.assignment.shopping_platform.dto;

import com.assignment.shopping_platform.repositroy.model.Order;
import com.assignment.shopping_platform.repositroy.model.OrderItem;
import lombok.Builder;
import org.joda.money.Money;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNullElseGet;

@Builder
public record OrderDto(
        String id,
        String email,
        Instant createdAt,
        List<OrderItemDto> items,
        List<TotalsDto> totalsByCurrency) {

    public OrderDto {
        items = requireNonNullElseGet(items, Collections::emptyList);
        totalsByCurrency = requireNonNullElseGet(totalsByCurrency, Collections::emptyList);
    }

    public record OrderItemDto(long itemId, String name, Money price) {
        static OrderItemDto from(OrderItem orderItem) {
            return new OrderItemDto(orderItem.getId(), orderItem.getProduct().getName(), Money.of(orderItem.getCurrency(), orderItem.getPrice()));
        }
    }

    public record TotalsDto(Money totalPrice) {
    }

    public static OrderDto from(Order order, List<Money> totalsPerCurrency) {
        return OrderDto.builder()
                .id(order.getExternalId().toString())
                .email(order.getEmail())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream()
                        .map(OrderItemDto::from)
                        .toList())
                .totalsByCurrency(totalsPerCurrency.stream()
                        .map(TotalsDto::new)
                        .toList())
                .build();
    }
}
