package com.assignment.shopping_platform.repositroy;

import com.assignment.shopping_platform.repositroy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByExternalId(UUID externalId);

    List<Product> findByExternalIdIn(Set<UUID> externalIds);
}
