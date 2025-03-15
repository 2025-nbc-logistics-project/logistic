package com.logistic.client.order.domain.repository;

import com.logistic.client.order.domain.model.Delivery;

public interface DeliveryRepository {
    void save(Delivery delivery);
}
