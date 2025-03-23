package com.logistic.client.alarm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrderInfoDto {
    private UUID orderId;         // 주문 Id
    private String username;      // 주문자 이름
    private List<ProductNameQuantity> slackOrderItems; // 상품(상품명, 수량) 리스트
    private String orderRequest;  // 주문 요청 사항

    private UUID departureHubId;  // 출발 허브 Id
    private List<UUID> transitHubs;  // 경유 허브 Id 리스트
    private AddressResponse destinationAddress; // 수령 업체 주소
    private String receiverCompanyName; // 수령 업체명
    private String deliveryManagerName; // 배송 담당자 이름

}
