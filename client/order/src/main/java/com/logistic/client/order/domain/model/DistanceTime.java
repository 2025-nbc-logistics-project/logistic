package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DistanceTime {
    private Double distance;     // km(또는 m) 단위
    private Integer time;        // 분(또는 시간) 단위

    public DistanceTime(Double distance, Integer time) {
        if (distance == null || distance < 0) {
            throw new IllegalArgumentException("거리(distance)는 0 이상이어야 합니다.");
        }
        if (time == null || time < 0) {
            throw new IllegalArgumentException("소요 시간(time)은 0 이상이어야 합니다.");
        }
        this.distance = distance;
        this.time = time;
    }
}
