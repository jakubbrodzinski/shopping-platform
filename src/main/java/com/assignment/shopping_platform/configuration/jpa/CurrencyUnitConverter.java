package com.assignment.shopping_platform.configuration.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.joda.money.CurrencyUnit;

@Converter(autoApply = true)
public class CurrencyUnitConverter implements AttributeConverter<CurrencyUnit, String> {
    @Override
    public String convertToDatabaseColumn(CurrencyUnit currencyUnit) {
        return currencyUnit != null ? currencyUnit.getCode() : null;
    }

    @Override
    public CurrencyUnit convertToEntityAttribute(String currencyCode) {
        return currencyCode != null ? CurrencyUnit.of(currencyCode) : null;
    }
}
