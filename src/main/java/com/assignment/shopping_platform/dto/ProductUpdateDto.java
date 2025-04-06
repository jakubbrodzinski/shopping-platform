package com.assignment.shopping_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.joda.money.Money;

@Builder
public record ProductUpdateDto(
        @NotBlank String name,
        @NotBlank String description,
        @PositiveOrZero Money price) {
}
