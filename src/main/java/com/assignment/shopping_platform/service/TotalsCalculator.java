package com.assignment.shopping_platform.service;

import com.assignment.shopping_platform.repositroy.model.Order;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
class TotalsCalculator {
    List<Money> calculateTotals(Order order) {
        Map<CurrencyUnit, Money> totalsByCurrency = order.getItems()
                .stream()
                .map(item -> Money.of(item.getCurrency(), item.getPrice()))
                .collect(toMap(Money::getCurrencyUnit, identity(), Money::plus));

        return new ArrayList<>(totalsByCurrency.values());
    }
}
