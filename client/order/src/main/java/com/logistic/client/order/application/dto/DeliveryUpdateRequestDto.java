package com.logistic.client.order.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliveryUpdateRequestDto {
    private UUID receiverDeliveryManagerId;
    private UUID supplierDeliveryManagerId;

    // 배송지 정보
    private String receiverPostalCode;
    private String receiverStreetAddress;
    private String receiverDetailAddress;

    private String recipientName;
    private UUID recipientSlackId;
}
