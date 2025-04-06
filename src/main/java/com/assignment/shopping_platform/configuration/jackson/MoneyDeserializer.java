package com.assignment.shopping_platform.configuration.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;
import java.math.BigDecimal;

class MoneyDeserializer extends StdDeserializer<Money> {

    public MoneyDeserializer() {
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException {
        ObjectCodec codec = jsonparser.getCodec();
        JsonNode node = codec.readTree(jsonparser);
        try {
            return Money.of(CurrencyUnit.of(node.get("currency").asText()), new BigDecimal(node.get("amount").asText()));
        } catch (Exception e) {
            throw JsonMappingException.from(deserializationcontext, "Exception when parsing money value: " + node.toString(), e);
        }
    }
}