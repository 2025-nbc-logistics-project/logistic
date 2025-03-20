package com.logistic.client.delivery.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryRouteStatus {
    HUB_WAITING("허브 이동 대기중", 1),
    HUB_MOVING("허브 이동중", 2),
    HUB_ARRIVED("목적지 허브 도착", 3);

    private final String description;
    private final int sequence;
}
