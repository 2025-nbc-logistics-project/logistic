package com.logistic.client.order.application.dto;

import com.logistic.client.order.domain.model.Money;
import com.logistic.client.order.domain.model.OrderItem;
import com.logistic.client.order.domain.model.Quantity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderItemDto {
    private UUID orderItemId;
    private UUID productId;
    private Quantity quantity;
    private Money price;
    private Money subTotal;

    public OrderItemDto(OrderItem orderItem) {
        this.orderItemId = orderItem.getOrderItemId();
        this.productId = orderItem.getProductId();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
        this.subTotal = orderItem.getSubTotal();
    }
}
