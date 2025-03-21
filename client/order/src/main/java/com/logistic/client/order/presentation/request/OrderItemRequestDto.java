package com.logistic.client.order.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    private UUID productId;
    private Integer quantity;
}
