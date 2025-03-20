package com.logistic.client.company.infrastructure.repository.product;

import com.logistic.client.company.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId);
}
