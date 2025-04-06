package com.assignment.shopping_platform.dto;

import com.assignment.shopping_platform.repositroy.model.Product;
import lombok.Builder;
import org.joda.money.Money;

@Builder
public record ProductDto(
        String id,
        String name,
        String description,
        Money price) {

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getExternalId().toString())
                .name(product.getName())
                .description(product.getDescription())
                .price(Money.of(product.getCurrency(), product.getPrice()))
                .build();
    }
}
