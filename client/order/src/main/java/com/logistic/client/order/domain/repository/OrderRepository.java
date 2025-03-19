package com.logistic.client.order.domain.repository;

import com.logistic.client.order.application.dto.OrderSearchDto;
import com.logistic.client.order.domain.model.Order;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> findById(UUID orderId);

    Page<Order> searchOrders(OrderSearchDto searchDto);
}
