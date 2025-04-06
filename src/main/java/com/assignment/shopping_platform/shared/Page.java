package com.assignment.shopping_platform.shared;

import static java.util.Optional.ofNullable;

public record Page(int pageNumber, int pageSize) {
    public Page(Integer pageNumber, Integer pageSize, int defaultPageSize) {
        this(ofNullable(pageNumber).orElse(0), ofNullable(pageSize).orElse(defaultPageSize));
    }

    public Page {
        if (pageSize <= 0 || pageNumber < 0) {
            throw new IllegalArgumentException();
        }
    }
}
