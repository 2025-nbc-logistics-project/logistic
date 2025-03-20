package com.logistic.client.order.presentation.request;

import com.logistic.client.order.presentation.request.OrderItemRequestDto;
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
