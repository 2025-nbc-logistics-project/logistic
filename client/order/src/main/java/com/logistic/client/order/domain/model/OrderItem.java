package com.logistic.client.order.domain.model;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class OrderItem {
    private UUID orderItemId;
    private UUID productId;
    private Quantity quantity;
    private Money price;
    private Money subTotal;

    public OrderItem(UUID productId, Quantity quantity, Money price) {
        this.orderItemId = UUID.randomUUID();
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = price.multiply(quantity.getQuantity());
    }

    public UUID getOrderItemId() {
        return orderItemId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }
}
