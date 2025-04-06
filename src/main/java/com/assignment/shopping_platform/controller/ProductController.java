package com.assignment.shopping_platform.controller;

import com.assignment.shopping_platform.dto.ProductDto;
import com.assignment.shopping_platform.dto.ProductUpdateDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.service.ProductService;
import com.assignment.shopping_platform.shared.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductDto> getProducts(@RequestParam(required = false, name = "pageSize") Integer pageSize,
                                        @RequestParam(required = false, name = "pageNumber") Integer pageNumber) {

        return productService.query(new Page(pageNumber, pageSize, DEFAULT_PAGE_SIZE));
    }

    @PostMapping("/products")
    public ProductDto createProduct(@Validated @RequestBody ProductUpdateDto productUpdateDto) {
        return productService.save(productUpdateDto);
    }

    @PutMapping("/products/{productId}")
    public ProductDto updateProduct(@PathVariable("productId") String productId,
                                    @Validated @RequestBody ProductUpdateDto productUpdateDto) {
        return productService.update(UUID.fromString(productId), productUpdateDto);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND).body(ex.getMessage());
    }
}
