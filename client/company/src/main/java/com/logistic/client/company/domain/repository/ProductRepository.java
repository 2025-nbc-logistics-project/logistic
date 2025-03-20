package com.logistic.client.company.domain.repository;

import com.logistic.client.company.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId);
    Page<Product> getProducts(Pageable pageable, String sortBy, String order);
}
