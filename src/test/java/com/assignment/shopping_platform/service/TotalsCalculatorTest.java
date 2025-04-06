package com.assignment.shopping_platform.service;

import com.assignment.shopping_platform.repositroy.model.Order;
import com.assignment.shopping_platform.repositroy.model.OrderItem;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static com.assignment.shopping_platform.TestFixtures.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.CurrencyUnit.USD;

class OrderCalculatorTest {
    private final TotalsCalculator calculator = new TotalsCalculator();

    @Test
    void shouldReturnEmptyListForEmptyOrder() {
        var emptyOrder = new Order();
        emptyOrder.setItems(List.of());

        var totals = calculator.calculateTotals(emptyOrder);

        assertThat(totals).isEmpty();
    }

    @Test
    void shouldSumSingleCurrencyItems() {
        var order = orderWithItems(
                item(USD, "10.00"),
                item(USD, "20.00"),
                item(USD, "5.50"));

        var totals = calculator.calculateTotals(order);

        assertThat(totals).containsExactly(USD("35.50"));
    }

    @Test
    void shouldHandleMultipleCurrencies() {
        Order order = orderWithItems(
                item(USD, "10.00"),
                item(EUR, "15.00"),
                item(USD, "5.00"),
                item(EUR, "5.00"));

        var totals = calculator.calculateTotals(order);

        assertThat(totals).containsExactlyInAnyOrder(USD("15.00"), EUR("20.00"));
    }

    @Test
    void shouldHandleZeroAmounts() {
        Order order = orderWithItems(
                item(USD, "0.00"),
                item(USD, "10.00"),
                item(EUR, "0.00")
        );

        var totals = calculator.calculateTotals(order);

        assertThat(totals)
                .containsExactlyInAnyOrder(
                        USD("10.00"),
                        EUR("0.00")
                );
    }

    private static Order orderWithItems(OrderItem... items) {
        Order order = new Order();
        order.setItems(List.of(items));
        return order;
    }

    private static OrderItem item(CurrencyUnit currency, String amount) {
        OrderItem item = new OrderItem();
        item.setCurrency(currency);
        item.setPrice(new BigDecimal(amount));
        return item;
    }
}