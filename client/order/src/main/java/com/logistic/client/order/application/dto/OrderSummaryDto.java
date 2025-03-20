package com.logistic.client.order.application.dto;

import com.logistic.client.order.domain.model.CompanyInfo;
import com.logistic.client.order.domain.model.Money;
import com.logistic.client.order.domain.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderSummaryDto {
    private UUID orderId;
    private CompanyInfo companyInfo;
    private Money totalPrice;
    private String orderRequest;

    public OrderSummaryDto(Order order) {
        this.orderId = order.getOrderId();
        this.companyInfo = order.getCompanyInfo();
        this.totalPrice = order.getTotalPrice();
        this.orderRequest = order.getOrderRequest();
    }
}
