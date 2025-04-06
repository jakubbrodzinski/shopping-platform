package com.assignment.shopping_platform.configuration.jpa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.CAD;
import static org.joda.money.CurrencyUnit.EUR;

class CurrencyUnitConverterTest {
    private final CurrencyUnitConverter converter = new CurrencyUnitConverter();

    @Test
    void shouldConvertToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(CAD)).isEqualTo("CAD");
    }

    @Test
    void shouldHandleNullWhenConvertingToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    void shouldConvertToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute("EUR")).isEqualTo(EUR);
    }

    @Test
    void shouldHandleNullWhenConvertingToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }
}