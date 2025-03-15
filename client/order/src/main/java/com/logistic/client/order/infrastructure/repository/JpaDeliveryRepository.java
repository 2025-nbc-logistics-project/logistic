package com.logistic.client.order.infrastructure.repository;

import com.logistic.client.order.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID> {
}
