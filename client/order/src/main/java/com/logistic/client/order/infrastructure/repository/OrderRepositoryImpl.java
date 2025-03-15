package com.logistic.client.order.infrastructure.repository;

import com.logistic.client.order.domain.model.Order;
import com.logistic.client.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void save(Order order) {
        jpaOrderRepository.save(order);
    }
}
