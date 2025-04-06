package com.assignment.shopping_platform.factory;

import com.assignment.shopping_platform.dto.ProductUpdateDto;
import com.assignment.shopping_platform.repositroy.model.Product;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductFactory {
    public Product create(ProductUpdateDto dto) {
        Product product = new Product();
        product.setCurrency(dto.price().getCurrencyUnit());
        product.setPrice(dto.price().getAmount());
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setExternalId(UUID.randomUUID());
        return product;
    }

    public Product update(Product product, ProductUpdateDto productUpdateDto) {
        product.setCurrency(productUpdateDto.price().getCurrencyUnit());
        product.setPrice(productUpdateDto.price().getAmount());
        product.setName(productUpdateDto.name());
        product.setDescription(productUpdateDto.description());
        return product;
    }
}
