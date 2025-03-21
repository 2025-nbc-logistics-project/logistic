package com.logistic.client.delivery.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {
    PENDING("배송 준비중", 1),
    WAITING_AT_HUB("허브 대기중", 2),
    MOVING_BETWEEN_HUB("허브 이동중", 3),
    ARRIVED_AT_DESTINATION_HUB("목적지 허브 도착", 4),
    MOVING_TO_COMPANY("업체로 이동중", 5),
    DELIVERY_COMPLETED("배송 완료", 6);

    private final String description;
    private final int sequence;
}
