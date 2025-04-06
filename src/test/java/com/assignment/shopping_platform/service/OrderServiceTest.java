package com.assignment.shopping_platform.service;

import com.assignment.shopping_platform.dto.OrderCreateDto;
import com.assignment.shopping_platform.dto.OrderDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.repositroy.ProductRepository;
import com.assignment.shopping_platform.repositroy.model.Product;
import com.assignment.shopping_platform.shared.Page;
import com.assignment.shopping_platform.utils.OrderMotherObject;
import jakarta.transaction.Transactional;
import org.assertj.core.groups.Tuple;
import org.joda.money.Money;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static com.assignment.shopping_platform.TestFixtures.USD;
import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderMotherObject orderMotherObject;

    @Autowired
    private OrderService orderService;

    @Nested
    class PlaceOrderTests {
        @Test
        void shouldCreateOrderWithValidItems() {
            var product1 = persistProduct("p-1", EUR("3.15"));
            var product2 = persistProduct("p-2", USD("2.01"));
            var orderCreateDto = OrderCreateDto.builder()
                    .email("customer@example.com")
                    .items(List.of(
                            new OrderCreateDto.OrderItemDto(product1.getExternalId().toString(), 1),
                            new OrderCreateDto.OrderItemDto(product2.getExternalId().toString(), 3)))
                    .build();
            var minValidTimestamp = Instant.now();


            var result = orderService.placeOrder(orderCreateDto);

            // Then
            assertThat(result.id()).isNotNull();
            assertThatCode(() -> UUID.fromString(result.id())).doesNotThrowAnyException();
            assertThat(result.email()).isEqualTo("customer@example.com");
            assertThat(result.createdAt()).isAfter(minValidTimestamp);
            assertThat(result.items())
                    .extracting(OrderDto.OrderItemDto::name, OrderDto.OrderItemDto::price)
                    .containsExactlyInAnyOrder(
                            Tuple.tuple("p-1", EUR("3.15")),
                            Tuple.tuple("p-2", USD("2.01")),
                            Tuple.tuple("p-2", USD("2.01")),
                            Tuple.tuple("p-2", USD("2.01")));
            assertThat(result.totalsByCurrency())
                    .extracting(OrderDto.TotalsDto::totalPrice)
                    .containsExactlyInAnyOrder(EUR("3.15"), USD("6.03"));
        }

        @Test
        void shouldFailWhenProductNotFound() {
            var orderCreateDto = OrderCreateDto.builder()
                    .email("customer@example.com")
                    .items(List.of(new OrderCreateDto.OrderItemDto(UUID.randomUUID().toString(), 1)))
                    .build();

            assertThatThrownBy(() -> orderService.placeOrder(orderCreateDto))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    class GetOrderTests {
        @Test
        void shouldReturnOrderThatWasCreatedWithinProvidedRange() {
            var p1 = persistProduct("p 1", USD("13.99"));
            var p2 = persistProduct("p 2", USD("0.12"));
            var order = orderMotherObject.createOrder(parse("2025-01-25T12:00:00Z"), p1, USD("12.00"), p2, USD("3.15"));

            var orderDtos = orderService.queryByCreatedAtTimestamp(parse("2025-01-24T12:00:00Z"), parse("2025-01-26T12:00:00Z"), new Page(0, 10));

            assertThat(orderDtos)
                    .singleElement()
                    .satisfies(orderDto -> {
                        assertThat(orderDto.id()).isEqualTo(order.getExternalId().toString());
                        assertThat(orderDto.email()).isEqualTo(order.getEmail());
                        assertThat(orderDto.createdAt()).isEqualTo(parse("2025-01-25T12:00:00Z"));
                        assertThat(orderDto.items())
                                .containsExactlyInAnyOrder(OrderDto.OrderItemDto.from(order.getItems().get(0)), OrderDto.OrderItemDto.from(order.getItems().get(1)));
                        assertThat(orderDto.totalsByCurrency())
                                .extracting(OrderDto.TotalsDto::totalPrice)
                                .containsExactly(USD("15.15"));
                    });
        }

        @Test
        void shouldPaginateBasedOnCreatedAtDescTimestamp() {
            var product = persistProduct("p 1", USD("13.99"));
            orderMotherObject.createOrder(parse("2025-01-23T12:00:00Z"), product, USD("12.00"));
            orderMotherObject.createOrder(parse("2025-01-25T12:00:00Z"), product, USD("15.00"), product, USD("3.15"));
            var order3 = orderMotherObject.createOrder(parse("2025-01-26T12:15:00Z"), product, USD("17.00"));

            var orderDtos = orderService.queryByCreatedAtTimestamp(parse("2025-01-24T12:00:00Z"), parse("2025-01-27T12:00:00Z"), new Page(0, 1));

            assertThat(orderDtos)
                    .singleElement()
                    .extracting(OrderDto::id)
                    .isEqualTo(order3.getExternalId().toString());
        }
    }

    private Product persistProduct(String name, Money price) {
        var product = new Product();
        product.setExternalId(UUID.randomUUID());
        product.setName(name);
        product.setDescription("Desc - " + name);
        product.setPrice(price.getAmount());
        product.setCurrency(price.getCurrencyUnit());
        return productRepository.save(product);
    }
}