package com.logistic.client.company.domain.repository;

import com.logistic.client.company.domain.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId);
}
