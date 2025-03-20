package com.logistic.client.company.infrastructure.repository.product;

import com.logistic.client.company.domain.model.Product;
import com.logistic.client.company.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final ProductQueryDSLRepositoryImpl productQueryDSLRepository;

    @Override
    public void save(Product product) {
        jpaProductRepository.save(product);
    }

}
