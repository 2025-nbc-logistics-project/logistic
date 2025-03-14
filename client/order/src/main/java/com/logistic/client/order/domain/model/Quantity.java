package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Quantity {
    private final Integer quantity;

    protected Quantity() {
        this.quantity = null;
    }

    public Quantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이여야 합니다.");
        }
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
