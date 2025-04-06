package com.assignment.shopping_platform.controller;

import com.assignment.shopping_platform.dto.OrderCreateDto;
import com.assignment.shopping_platform.dto.OrderDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.service.OrderService;
import com.assignment.shopping_platform.shared.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Operations related to Orders")
public class OrderController {
    private static final Integer DEFAULT_PAGE_SIZE = 5;
    private final OrderService orderService;

    @GetMapping("/orders")
    @Operation(summary = "Get orders within a time range",
            description = "Fetches a list of orders created between the 'from' and 'to' timestamps with optional pagination")
    @ApiResponse(responseCode = "200", description = "List of orders returned successfully")
    public List<OrderDto> getOrders(
            @Parameter(description = "Start timestamp (inclusive)", required = true)
            @RequestParam("from") Instant from,
            @Parameter(description = "End timestamp (inclusive)", required = true)
            @RequestParam("to") Instant to,
            @Parameter(description = "Page size (optional), defaults to 5")
            @RequestParam(required = false, name = "pageSize") Integer pageSize,
            @Parameter(description = "Page number (optional)")
            @RequestParam(required = false, name = "pageNumber") Integer pageNumber) {
        return orderService.queryByCreatedAtTimestamp(from, to, new Page(pageNumber, pageSize, DEFAULT_PAGE_SIZE));
    }

    @PostMapping("/orders")
    @Operation(
            summary = "Place a new order",
            description = "Creates and returns a new order based on the provided order data.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order details for creation",
                    content = @Content(schema = @Schema(implementation = OrderCreateDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order successfully created",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public OrderDto placeOrder(@Validated @RequestBody OrderCreateDto orderCreateDto) {
        return orderService.placeOrder(orderCreateDto);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }
}
