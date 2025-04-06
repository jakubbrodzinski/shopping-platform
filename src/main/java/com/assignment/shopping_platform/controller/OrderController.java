package com.assignment.shopping_platform.controller;

import com.assignment.shopping_platform.dto.OrderCreateDto;
import com.assignment.shopping_platform.dto.OrderDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.service.OrderService;
import com.assignment.shopping_platform.shared.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private static final Integer DEFAULT_PAGE_SIZE = 5;
    private final OrderService orderService;

    @GetMapping("/orders")
    public List<OrderDto> getOrders(@RequestParam("from") Instant from, @RequestParam("to") Instant to,
                                    @RequestParam(required = false, name = "pageSize") Integer pageSize,
                                    @RequestParam(required = false, name = "pageNumber") Integer pageNumber) {
        return orderService.queryByCreatedAtTimestamp(from, to, new Page(pageNumber, pageSize, DEFAULT_PAGE_SIZE));
    }

    @PostMapping("/orders")
    public OrderDto placeOrder(@Validated @RequestBody OrderCreateDto orderCreateDto) {
        return orderService.placeOrder(orderCreateDto);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }
}
