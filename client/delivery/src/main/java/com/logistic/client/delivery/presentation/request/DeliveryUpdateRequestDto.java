package com.logistic.client.delivery.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
