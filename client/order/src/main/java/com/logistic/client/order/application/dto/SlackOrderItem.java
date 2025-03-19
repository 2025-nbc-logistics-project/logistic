package com.logistic.client.order.application.dto;

import com.logistic.client.order.domain.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlackOrderItem {
    private UUID productId;
    private int quantity;

    public SlackOrderItem(OrderItem orderItem) {
        this.productId = orderItem.getProductId();
        this.quantity = orderItem.getQuantity().getQuantity();
    }
}
