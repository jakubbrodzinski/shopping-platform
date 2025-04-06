package com.assignment.shopping_platform.configuration.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.money.Money;

import java.io.IOException;

class MoneySerializer extends JsonSerializer<Money> {

    @Override
    public void serialize(Money money, JsonGenerator aJsonGenerator, SerializerProvider aSerializerProvider) throws IOException {
        aJsonGenerator.writeStartObject();
        aJsonGenerator.writeFieldName("currency");
        aJsonGenerator.writeString(money.getCurrencyUnit().getCode());
        aJsonGenerator.writeFieldName("amount");
        aJsonGenerator.writeString(money.getAmount().toPlainString());
        aJsonGenerator.writeEndObject();
    }
}
