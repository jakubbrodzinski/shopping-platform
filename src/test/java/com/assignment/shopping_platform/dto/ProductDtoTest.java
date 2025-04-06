package com.assignment.shopping_platform.dto;

import com.assignment.shopping_platform.repositroy.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.EUR;

class ProductDtoTest {
    @Test
    void shouldMapProductEntityToDto() {
        var product = new Product();
        product.setId(15L);
        product.setExternalId(UUID.fromString("f18c6adc-c6e6-464e-b5ea-f63ab7f9e49d"));
        product.setName("name");
        product.setDescription("description");
        product.setCurrency(EUR);
        product.setPrice(new BigDecimal("9.99"));

        var dto = ProductDto.from(product);

        assertThat(dto.id()).isEqualTo("f18c6adc-c6e6-464e-b5ea-f63ab7f9e49d");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.description()).isEqualTo("description");
        assertThat(dto.price()).isEqualTo(EUR("9.99"));
    }
}