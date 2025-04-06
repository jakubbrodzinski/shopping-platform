package com.assignment.shopping_platform;

import com.assignment.shopping_platform.repositroy.OrderRepository;
import com.assignment.shopping_platform.repositroy.ProductRepository;
import com.assignment.shopping_platform.repositroy.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.Arrays;

@TestComponent
public class OrderMotherObject {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(String...productIds){
//        Arrays.stream(productIds).d
        return null;
    }
}
