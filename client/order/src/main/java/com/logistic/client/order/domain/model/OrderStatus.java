package com.logistic.client.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("배송 준비중"),
    DELIVERING("배송 중"),
    COMPLETED("배송 완료"),
    CANCELLED("취소됨");

    private final String description;
}
