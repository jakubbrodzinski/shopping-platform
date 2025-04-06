package com.assignment.shopping_platform.controller;

import com.assignment.shopping_platform.dto.ProductDto;
import com.assignment.shopping_platform.dto.ProductUpdateDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.service.ProductService;
import com.assignment.shopping_platform.shared.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@Tag(name = "Products", description = "Operations related to Products")
public class ProductController {
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    private final ProductService productService;

    @GetMapping("/products")
    @Operation(
            summary = "Get a paginated list of products",
            description = "Returns a list of products with optional pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public List<ProductDto> getProducts(@RequestParam(required = false, name = "pageSize") Integer pageSize,
                                        @RequestParam(required = false, name = "pageNumber") Integer pageNumber) {

        return productService.query(new Page(pageNumber, pageSize, DEFAULT_PAGE_SIZE));
    }

    @PostMapping("/products")
    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with the given details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductUpdateDto.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body does not follow the schema/validation."),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ProductDto createProduct(@Validated @RequestBody ProductUpdateDto productUpdateDto) {
        return productService.save(productUpdateDto);
    }

    @PutMapping("/products/{productId}")
    @Operation(
            summary = "Update an existing product",
            description = "Updates a product by its ID with the given details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated product details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductUpdateDto.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid product ID or request body does not pass the validation"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ProductDto updateProduct(@PathVariable("productId") String productId,
                                    @Validated @RequestBody ProductUpdateDto productUpdateDto) {
        return productService.update(UUID.fromString(productId), productUpdateDto);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND).body(ex.getMessage());
    }
}
