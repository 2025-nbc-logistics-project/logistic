package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class Orderer {
    private final UUID userId;
    private final String username;

    protected Orderer() {
        this.userId = null;
        this.username = null;
    }

    public Orderer(UUID userId, String username) {
        if (userId == null || username == null) {
            throw new IllegalArgumentException("주문자의 정보가 올바르지 않습니다.");
        }
        this.userId = userId;
        this.username = username;
    }
}
