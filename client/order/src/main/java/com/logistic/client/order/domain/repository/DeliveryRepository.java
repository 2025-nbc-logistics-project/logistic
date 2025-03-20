package com.logistic.client.order.domain.repository;

import com.logistic.client.order.application.dto.DeliverySearchDto;
import com.logistic.client.order.domain.model.Delivery;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    void save(Delivery delivery);

    Optional<Delivery> findById(UUID deliveryId);

    Page<Delivery> searchDeliveries(DeliverySearchDto searchDto);
}
