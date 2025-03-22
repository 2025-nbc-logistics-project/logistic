package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
