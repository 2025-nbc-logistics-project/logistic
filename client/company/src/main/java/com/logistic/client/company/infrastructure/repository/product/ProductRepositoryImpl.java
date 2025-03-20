package com.logistic.client.company.infrastructure.repository.product;

import com.logistic.client.company.domain.model.Product;
import com.logistic.client.company.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final ProductQueryDSLRepositoryImpl productQueryDSLRepository;

    @Override
    public void save(Product product) {
        jpaProductRepository.save(product);
    }

    @Override
    public Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId) {
        return jpaProductRepository.findByProductIdAndDeletedAtIsNull(productId);
    }

}
