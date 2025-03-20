package com.logistic.client.company.infrastructure.repository.product;

import com.logistic.client.company.domain.model.product.Product;
import com.logistic.client.company.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Product> getProducts(Pageable pageable, String sortBy, String order) {
        return productQueryDSLRepository.getProducts(pageable, sortBy, order);
    }

    @Override
    public Page<Product> getSearchProducts(String key, UUID company, UUID hub, Pageable pageable, String sortBy, String order) {
        return productQueryDSLRepository.getSearchProducts(key, company, hub, pageable, sortBy, order);
    }

}
