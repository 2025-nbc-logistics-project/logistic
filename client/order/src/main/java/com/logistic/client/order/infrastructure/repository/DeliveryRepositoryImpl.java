package com.logistic.client.order.infrastructure.repository;

import com.logistic.client.order.domain.model.Delivery;
import com.logistic.client.order.domain.repository.DeliveryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final JpaDeliveryRepository jpaDeliveryRepository;

    public DeliveryRepositoryImpl(JpaDeliveryRepository jpaDeliveryRepository) {
        this.jpaDeliveryRepository = jpaDeliveryRepository;
    }

    @Override
    public void save(Delivery delivery) {
        jpaDeliveryRepository.save(delivery);
    }
}
