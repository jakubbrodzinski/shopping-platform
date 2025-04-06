package com.assignment.shopping_platform.dto;

import lombok.Builder;
import org.joda.money.Money;

@Builder
public record ProductUpdateDto(
        String name,
        String description,
        Money price) {
}
