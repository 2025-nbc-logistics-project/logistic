package com.logistic.client.order.domain.model;

public class Money {
    public static final Money ZERO = new Money(0);
    private final Integer amount;

    public Money(Integer amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이여야 합니다.");
        }
        this.amount = amount;
    }

    public Money multiply(Integer quantity) { return new Money(this.amount * quantity); }

    public Money add(Money amount) { return new Money(this.amount + amount.amount); }
}
