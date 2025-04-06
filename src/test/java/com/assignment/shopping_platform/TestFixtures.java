package com.assignment.shopping_platform;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;

public class TestFixtures {
    public static Money EUR(String money) {
        return Money.of(CurrencyUnit.EUR, new BigDecimal(money));
    }
}
