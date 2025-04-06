package com.assignment.shopping_platform.repositroy;

import com.assignment.shopping_platform.repositroy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
