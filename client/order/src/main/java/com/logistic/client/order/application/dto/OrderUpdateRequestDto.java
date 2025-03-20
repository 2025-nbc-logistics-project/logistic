package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateRequestDto {
    private String orderRequest;
    private List<OrderItemRequestDto> orderItems;
}
