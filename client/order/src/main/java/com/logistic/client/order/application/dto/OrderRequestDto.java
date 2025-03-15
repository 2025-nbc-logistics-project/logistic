package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private UUID receiverCompanyId;
    private UUID supplierCompanyId;
    List<OrderItemRequestDto> orderItems;
    private String orderRequest;
}
