package com.logistic.client.order.domain.repository;

import com.logistic.client.order.domain.model.Order;

public interface OrderRepository {
    void save(Order order);
}
