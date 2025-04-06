package com.assignment.shopping_platform.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.money.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JacksonConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(jodaMoneySerializationModule());
        return objectMapper;
    }

    private static SimpleModule jodaMoneySerializationModule() {
        return new SimpleModule()
                .addSerializer(Money.class, new MoneySerializer())
                .addDeserializer(Money.class, new MoneyDeserializer());
    }
}