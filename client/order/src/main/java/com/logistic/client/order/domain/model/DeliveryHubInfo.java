package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class DeliveryHubInfo {
    private final UUID departureHubId; // 출발 허브 ID
    private final UUID destinationHubId; // 최종 목적지 허브 ID

    protected DeliveryHubInfo() {
        this.departureHubId = null;
        this.destinationHubId = null;
    }

    public DeliveryHubInfo(UUID departureHubId, UUID destinationHubId) {
        if (departureHubId == null || destinationHubId == null || departureHubId.equals(destinationHubId)) {
            throw  new IllegalArgumentException("허브 정보를 불러오는 과정에 오류가 발생했습니다.");
        }
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
    }
}
