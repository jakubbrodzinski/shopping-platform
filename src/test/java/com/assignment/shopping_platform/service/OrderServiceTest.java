package com.assignment.shopping_platform.service;

import com.assignment.shopping_platform.dto.OrderCreateDto;
import com.assignment.shopping_platform.dto.OrderDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.repositroy.OrderRepository;
import com.assignment.shopping_platform.repositroy.ProductRepository;
import com.assignment.shopping_platform.repositroy.model.Product;
import jakarta.transaction.Transactional;
import org.assertj.core.groups.Tuple;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static com.assignment.shopping_platform.TestFixtures.USD;
import static org.assertj.core.api.Assertions.*;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.CurrencyUnit.USD;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Nested
    class PlaceOrderTests {
        @Test
        void shouldCreateOrderWithValidItems() {
            var product1 = persistProduct("p-1", "3.15", EUR);
            var product2 = persistProduct("p-2", "2.01", USD);
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
//
//    @Nested
//    class GetOrderTests {
//        private UUID existingOrderId;
//
//        @BeforeEach
//        void setupOrder() {
//            OrderCreateDto createDto = new OrderCreateDto(
//                    "customer@example.com",
//                    List.of(new OrderItemCreateDto(existingProductId, 1))
//            );
//            existingOrderId = UUID.fromString(orderService.placeOrder(createDto).id());
//        }
//
//        @Test
//        void shouldCalculateTotalsCorrectlyForMultiCurrency() {
//            // Add EUR product
//            Product eurProduct = productRepository.save(new Product(
//                    "EUR Product", "Desc", Money.of(CurrencyUnit.EUR, BigDecimal.TEN))
//            );
//
//            OrderCreateDto multiCurrencyOrder = new OrderCreateDto(
//                    "customer@example.com",
//                    List.of(
//                            new OrderItemCreateDto(existingProductId, 1), // $19.99
//                            new OrderItemCreateDto(eurProduct.getExternalId(), 3) // €10 x 3
//                    )
//            );
//
//            OrderDto result = orderService.placeOrder(multiCurrencyOrder);
//
//            Assertions.assertThat(result.totalsByCurrency())
//                    .hasSize(2)
//                    .extracting(TotalsDto::totalPrice)
//                    .containsExactlyInAnyOrder(
//                            Money.of(CurrencyUnit.USD, BigDecimal.valueOf(19.99)),
//                            Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(30.00))
//                    );
//        }
//    }

    private Product persistProduct(String name, String price, CurrencyUnit currencyUnit) {
        var product = new Product();
        product.setExternalId(UUID.randomUUID());
        product.setName(name);
        product.setDescription("Desc - " + name);
        product.setPrice(new BigDecimal(price));
        product.setCurrency(currencyUnit);
        return productRepository.save(product);
    }
}