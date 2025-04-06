package com.assignment.shopping_platform.factory;

import com.assignment.shopping_platform.dto.ProductUpdateDto;
import com.assignment.shopping_platform.repositroy.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static com.assignment.shopping_platform.TestFixtures.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.CurrencyUnit.USD;

class ProductFactoryTest {
    private final ProductFactory productFactory = new ProductFactory();

    @Test
    void shouldCreateProductFromDto() {
        var updateDto = ProductUpdateDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(EUR("19.99"))
                .build();

        var product = productFactory.create(updateDto);

        assertThat(product.getName()).isEqualTo("Test Product");
        assertThat(product.getDescription()).isEqualTo("Test Description");
        assertThat(product.getCurrency()).isEqualTo(EUR);
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("19.99"));
        assertThat(product.getExternalId()).isNotNull();
        assertThat(product.getId()).isNull();
    }

    @Test
    void shouldUpdateTheProductBasedOnDto() {
        var existingProduct = new Product();
        existingProduct.setId(15L);
        existingProduct.setExternalId(UUID.fromString("f18c6adc-c6e6-464e-b5ea-f63ab7f9e49d"));
        existingProduct.setName("Old Name");
        existingProduct.setDescription("Old Description");
        existingProduct.setCurrency(EUR);
        existingProduct.setPrice(new BigDecimal("9.99"));

        var updateDto = ProductUpdateDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(USD("19.99"))
                .build();

        var updatedProduct = productFactory.update(existingProduct, updateDto);

        // Assert
        assertThat(updatedProduct.getName()).isEqualTo("Test Product");
        assertThat(updatedProduct.getDescription()).isEqualTo("Test Description");
        assertThat(updatedProduct.getCurrency()).isEqualTo(USD);
        assertThat(updatedProduct.getPrice()).isEqualTo(new BigDecimal("19.99"));
        assertThat(updatedProduct.getExternalId()).isEqualTo(UUID.fromString("f18c6adc-c6e6-464e-b5ea-f63ab7f9e49d"));
        assertThat(updatedProduct.getId()).isEqualTo(15L);
    }
}