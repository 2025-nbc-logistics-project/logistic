package com.logistic.client.order.application.dto;

import com.logistic.client.order.domain.model.CompanyInfo;
import com.logistic.client.order.domain.model.Money;
import com.logistic.client.order.domain.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private UUID orderId;
    private CompanyInfo companyInfo;
    private Money totalPrice;
    private String orderRequest;
    private List<OrderItemDto> orderItems;

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.companyInfo = order.getCompanyInfo();
        this.totalPrice = order.getTotalPrice();
        this.orderRequest = order.getOrderRequest();
        this.orderItems = order.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
    }
}
