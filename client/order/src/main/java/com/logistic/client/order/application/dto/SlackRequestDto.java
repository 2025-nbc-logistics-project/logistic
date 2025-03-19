package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlackRequestDto {
    private UUID orderId;         // 주문 Id
    private String username;      // 주문자 이름
    private String email;         // 주문자 이메일
    private List<SlackOrderItem> slackOrderItems; // 상품(Id, 수량) 리스트
    private String orderRequest;  // 주문 요청 사항

    private UUID departureHubId;  // 출발 허브 Id
    private List<UUID> transitHubs;  // 경유 허브 Id 리스트
    private AddressResponse destinationAddress; // 수령 업체 주소
    private UUID supplierCompanyId; // 수령 업체 Id (슬랙 서비스에서 해당 Id를 가지고 배송 담당자 정보 조회)
}
