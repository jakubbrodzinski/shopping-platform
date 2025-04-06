package com.assignment.shopping_platform.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNullElseGet;

@Builder
public record OrderCreateDto(
        @Email String email,
        @NotEmpty @Valid List<OrderItemDto> items) {

    public OrderCreateDto {
        items = requireNonNullElseGet(items, Collections::emptyList);
    }

    public record OrderItemDto(@UUID String productId, @Positive @NotNull Integer quantity) {
    }
}
