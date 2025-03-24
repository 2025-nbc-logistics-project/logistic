package com.logistic.client.alarm.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class AiRequestDto {
    private String orderRequest;  // 주문 요청 사항
    private UUID departureHubId;  // 출발 허브 Id
    private List<String> transitHubs;  // 경유 허브명 리스트
    private AddressResponse destinationAddress; // 수령 업체 주소

    public AiRequestDto(OrderInfoDto orderInfoDto, List<String> transitHubs) {
        this.orderRequest = orderInfoDto.getOrderRequest();
        this. departureHubId = orderInfoDto.getDepartureHubId();
        this.transitHubs = transitHubs;
        this.destinationAddress = orderInfoDto.getDestinationAddress();
    }
}
