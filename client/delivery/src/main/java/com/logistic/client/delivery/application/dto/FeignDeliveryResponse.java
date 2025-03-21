package com.logistic.client.delivery.application.dto;

import com.logistic.client.delivery.domain.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class FeignDeliveryResponse {
    private UUID deliveryId;
    private UUID orderId;
    private String status;

    private UUID receiverDeliveryManagerId; // 수령 업체 배송 담당자 ID
    private UUID supplierDeliveryManagerId; // 공급 업체 배송 담당자 ID
    private UUID departureHubId; // 출발 허브 ID
    private UUID destinationHubId; // 최종 목적지 허브 ID

    private String receiverPostalCode;
    private String receiverDetailAddress;
    private String receiverStreetAddress;

    private String supplierPostalCode;
    private String supplierDetailAddress;
    private String supplierStreetAddress;

    // 포함된 배송 경로 목록
    private List<FeignDeliveryRouteResponse> deliveryRoutes;

    public FeignDeliveryResponse(Delivery delivery) {
        this.deliveryId = delivery.getDeliveryId();
        this.orderId = delivery.getOrderId();
        this.status = delivery.getStatus().name();
        this.receiverDeliveryManagerId = delivery.getDeliveryManagerId().getReceiverDeliveryManagerId();
        this.supplierDeliveryManagerId = delivery.getDeliveryManagerId().getSupplierDeliveryManagerId();
        this.departureHubId = delivery.getDeliveryHubInfo().getDepartureHubId();
        this.destinationHubId = delivery.getDeliveryHubInfo().getDestinationHubId();
        this.receiverPostalCode = delivery.getShippingInfo().getReceiverAddress().getPostalCode();
        this.receiverDetailAddress = delivery.getShippingInfo().getReceiverAddress().getDetailAddress();
        this.receiverStreetAddress = delivery.getShippingInfo().getReceiverAddress().getStreetAddress();
        this.supplierPostalCode = delivery.getShippingInfo().getSupplierAddress().getPostalCode();
        this.supplierDetailAddress = delivery.getShippingInfo().getSupplierAddress().getDetailAddress();
        this.supplierStreetAddress = delivery.getShippingInfo().getSupplierAddress().getStreetAddress();
        // 배송 경로 Dto
        this.deliveryRoutes = delivery.getDeliveryRoutes().stream()
            .map(FeignDeliveryRouteResponse::new)
            .collect(Collectors.toList());
    }
}
