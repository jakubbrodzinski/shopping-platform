package com.assignment.shopping_platform.factory;

import com.assignment.shopping_platform.dto.OrderCreateDto;
import com.assignment.shopping_platform.repositroy.model.Product;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.CurrencyUnit.USD;

class OrderFactoryTest {
    private final OrderFactory factory = new OrderFactory();

    @Test
    void shouldCreateOrderWithBasicMetadata() {
        var dto = OrderCreateDto.builder()
                .email("test@example.com")
                .build();
        var minValidTimestamp = Instant.now();

        var order = factory.create(dto, emptyList());

        assertThat(order.getEmail()).isEqualTo("test@example.com");
        assertThat(order.getId()).isNull();
        assertThat(order.getExternalId()).isNotNull();
        assertThat(order.getCreatedAt()).isAfter(minValidTimestamp);
    }

    @Test
    void shouldCreateOrderItemsForSingleProduct() {
        var product1 = product("p1", USD, "10.00");
        var product2 = product("p2", EUR, "15.00");

        var dto = OrderCreateDto.builder()
                .email("test@example.com")
                .items(List.of(
                        new OrderCreateDto.OrderItemDto(product1.getExternalId().toString(), 2),
                        new OrderCreateDto.OrderItemDto(product2.getExternalId().toString(), 1)))
                .build();

        var result = factory.create(dto, List.of(product1, product2));

        assertThat(result.getItems())
                .hasSize(3)
                .satisfiesExactlyInAnyOrder(
                        orderItem -> {
                            assertThat(orderItem.getCurrency()).isEqualTo(USD);
                            assertThat(orderItem.getPrice()).isEqualTo(new BigDecimal("10.00"));
                            assertThat(orderItem.getOrder()).isSameAs(result);
                            assertThat(orderItem.getProduct()).isSameAs(product1);
                            assertThat(orderItem.getId()).isNull();
                        },
                        orderItem -> {
                            assertThat(orderItem.getCurrency()).isEqualTo(USD);
                            assertThat(orderItem.getPrice()).isEqualTo(new BigDecimal("10.00"));
                            assertThat(orderItem.getOrder()).isSameAs(result);
                            assertThat(orderItem.getProduct()).isSameAs(product1);
                            assertThat(orderItem.getId()).isNull();
                        },
                        orderItem -> {
                            assertThat(orderItem.getCurrency()).isEqualTo(EUR);
                            assertThat(orderItem.getPrice()).isEqualTo(new BigDecimal("15.00"));
                            assertThat(orderItem.getOrder()).isSameAs(result);
                            assertThat(orderItem.getProduct()).isSameAs(product2);
                            assertThat(orderItem.getId()).isNull();
                        });
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        var createDto = OrderCreateDto.builder()
                .email("test@example.com")
                .items(List.of(new OrderCreateDto.OrderItemDto("nonexistent-id", 1)))
                .build();


        assertThatThrownBy(() -> factory.create(createDto, emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Product product(String name, CurrencyUnit currency, String price) {
        Product product = new Product();
        product.setExternalId(UUID.randomUUID());
        product.setName(name);
        product.setCurrency(currency);
        product.setPrice(new BigDecimal(price));
        return product;
    }
}