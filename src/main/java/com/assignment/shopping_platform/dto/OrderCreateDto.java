package com.assignment.shopping_platform.dto;

import lombok.Builder;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNullElseGet;

@Builder
public record OrderCreateDto(
        String email,
        List<OrderItemDto> items) {

    public OrderCreateDto {
        items = requireNonNullElseGet(items, Collections::emptyList);
    }

    public record OrderItemDto(String productId, int quantity) {
    }
}
