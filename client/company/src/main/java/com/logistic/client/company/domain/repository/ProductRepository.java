package com.logistic.client.company.domain.repository;

import com.logistic.client.company.domain.model.Product;

public interface ProductRepository {
    void save(Product product);
}
