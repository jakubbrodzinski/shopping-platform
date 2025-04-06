package com.assignment.shopping_platform.repositroy;

import com.assignment.shopping_platform.repositroy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
