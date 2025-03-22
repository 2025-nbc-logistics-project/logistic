package com.logistic.client.order.presentation.request;

import com.logistic.client.order.presentation.request.OrderItemRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private UUID receiverCompanyId;
    private UUID supplierCompanyId;
    List<OrderItemRequestDto> orderItems;
    private String orderRequest;
}
