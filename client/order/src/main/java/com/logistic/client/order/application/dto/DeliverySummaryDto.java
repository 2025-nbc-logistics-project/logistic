package com.logistic.client.order.application.dto;

import com.logistic.client.order.domain.model.Delivery;
import com.logistic.client.order.domain.model.DeliveryManagerId;
import com.logistic.client.order.domain.model.DeliveryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DeliverySummaryDto {
    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus status;
    private DeliveryManagerId deliveryManagerId;

    public DeliverySummaryDto(Delivery delivery) {
        this.deliveryId = delivery.getDeliveryId();
        this.orderId = delivery.getOrderId();
        this.status = delivery.getStatus();
        this.deliveryManagerId = delivery.getDeliveryManagerId();
    }
}
