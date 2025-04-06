package com.assignment.shopping_platform.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Product with id=%s not found";

    public ProductNotFoundException(UUID productId) {
        super(ERROR_MESSAGE.formatted(productId));
    }

    public ProductNotFoundException(){

    }
}
