package com.logistic.client.order.application.dto;

import com.logistic.client.order.domain.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class DeliveryResponseDto {
    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus status;

    private DeliveryManagerId deliveryManagerId;
    private DeliveryHubInfo deliveryHubInfo;
    private ShippingInfo shippingInfo;

    // 포함된 배송 경로 목록
    private List<DeliveryRouteDto> deliveryRoutes;

    public DeliveryResponseDto(Delivery delivery) {
        this.deliveryId = delivery.getDeliveryId();
        this.orderId = delivery.getOrderId();
        this.status = delivery.getStatus();
        this.deliveryManagerId = delivery.getDeliveryManagerId();
        this.deliveryHubInfo = delivery.getDeliveryHubInfo();
        this.shippingInfo = delivery.getShippingInfo();
        // 배송 경로 Dto
        this.deliveryRoutes = delivery.getDeliveryRoutes().stream()
            .map(DeliveryRouteDto::new)
            .collect(Collectors.toList());
    }
}
