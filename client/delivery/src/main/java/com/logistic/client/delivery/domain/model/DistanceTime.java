package com.logistic.client.delivery.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class DistanceTime {
    private final double distance;     // km(또는 m) 단위
    private final double time;        // 분(또는 시간) 단위

    protected DistanceTime() {
        this.distance = 0;
        this.time = 0;
    }

    public DistanceTime(double distance, double time) {
        if (distance < 0) {
            throw new IllegalArgumentException("거리(distance)는 0 이상이어야 합니다.");
        }
        if (time < 0) {
            throw new IllegalArgumentException("소요 시간(time)은 0 이상이어야 합니다.");
        }
        this.distance = distance;
        this.time = time;
    }
}
