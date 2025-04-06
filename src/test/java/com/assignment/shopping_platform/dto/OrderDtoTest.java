package com.assignment.shopping_platform.dto;

import com.assignment.shopping_platform.repositroy.model.Order;
import com.assignment.shopping_platform.repositroy.model.OrderItem;
import com.assignment.shopping_platform.repositroy.model.Product;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static com.assignment.shopping_platform.TestFixtures.USD;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.CurrencyUnit.USD;

class OrderDtoTest {
    @Test
    void shouldMapOrdersMetadata() {
        var order = new Order();
        order.setItems(emptyList());
        order.setEmail("email@something.com");
        order.setCreatedAt(Instant.parse("2025-01-19T12:31:54Z"));
        order.setExternalId(UUID.fromString("cdb2fe82-8675-48d9-931c-937230ba9eec"));

        var dto = OrderDto.from(order, emptyList());

        assertThat(dto.email()).isEqualTo("email@something.com");
        assertThat(dto.createdAt()).isEqualTo(Instant.parse("2025-01-19T12:31:54Z"));
        assertThat(dto.id()).isEqualTo("cdb2fe82-8675-48d9-931c-937230ba9eec");
    }

    @Test
    void shouldMapTotals() {
        var order = new Order();
        order.setItems(emptyList());
        order.setExternalId(UUID.randomUUID());

        var dto = OrderDto.from(order, List.of(EUR("1.99"), USD("300.19")));

        assertThat(dto.totalsByCurrency())
                .extracting(OrderDto.TotalsDto::totalPrice)
                .containsExactlyInAnyOrder(EUR("1.99"), USD("300.19"));
    }

    @Test
    void shouldMapItems() {
        var order = new Order();
        order.setExternalId(UUID.randomUUID());
        order.setItems(List.of(
                orderItem("p-1", 1, EUR, "0.13"),
                orderItem("p-1", 5, EUR, "0.11"),
                orderItem("p-3", 19, USD, "1.15")));

        var dto = OrderDto.from(order, emptyList());

        assertThat(dto.items())
                .containsExactlyInAnyOrder(
                        new OrderDto.OrderItemDto(1, "p-1", EUR("0.13")),
                        new OrderDto.OrderItemDto(5, "p-1", EUR("0.11")),
                        new OrderDto.OrderItemDto(19, "p-3", USD("1.15")));
    }

    private OrderItem orderItem(String productName, long id, CurrencyUnit currency, String price) {
        var orderItem = new OrderItem();
        var product = new Product();
        product.setName(productName);
        orderItem.setProduct(product);
        orderItem.setId(id);
        orderItem.setCurrency(currency);
        orderItem.setPrice(new BigDecimal(price));
        return orderItem;
    }

}
