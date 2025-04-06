package com.assignment.shopping_platform.repositroy;

import com.assignment.shopping_platform.repositroy.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.externalId = :externalId")
    Optional<Order> findByExternalId(UUID externalId);

    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :from AND :to")
    Page<Order> findByCreatedAtBetween(Instant from, Instant to, Pageable pageable);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o IN :orderIds")
    List<Order> fetchOrdersWithAssociations(Set<Long> orderIds);
}
