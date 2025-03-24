package com.logistic.client.ai.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AiRequestDto {

    private UUID orderId;         // 주문 Id
    private String username;      // 주문자 이름
    private List<ProductNameQuantity> slackOrderItems; // 상품(상품명, 수량) 리스트
    private String orderRequest;  // 주문 요청 사항

    private String departureHubName; // 출발 허브
    private List<String> transitHubs; // 경유 허브 리스트
    private AddressResponse destinationAddress; // 수령 업체 주소
    private String receiverCompanyName; // 수령 업체명
    private String deliveryManagerName; // 배송 담당자 이름

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductNameQuantity {
        private String productName;
        private int quantity;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressResponse {
        private String postalCode;
        private String detailAddress;
        private String streetAddress;
    }
}
