package com.logistic.client.order.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderUpdateRequestDto {
    private String orderRequest;
    private List<OrderItemRequestDto> orderItems;
}
