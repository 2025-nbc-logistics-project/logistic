package com.logistic.client.order.domain.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_order_item")
public class OrderItem {
    @Id
    private UUID orderItemId;

    private UUID productId;

    @Embedded
    private Quantity quantity;

    @Embedded
    private Money price;

    @Embedded
    private Money subTotal;

    public OrderItem(UUID productId, Quantity quantity, Money price) {
        this.orderItemId = UUID.randomUUID();
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = price.multiply(quantity.getQuantity()); // 소계 계산
    }
}
