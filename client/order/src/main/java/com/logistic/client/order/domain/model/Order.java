package com.logistic.client.order.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
public class Order {
    private UUID orderId;
    private CompanyInfo companyInfo;
    private List<OrderItem> orderItems;
    private String orderRequest;
    private Money totalPrice;

    public Order(CompanyInfo companyInfo, List<OrderItem> orderItems, String orderRequest) {
        this.orderId = UUID.randomUUID();
        this.companyInfo = companyInfo;
        this.orderItems = orderItems;
        this.orderRequest = orderRequest;
        this.totalPrice = orderItems.stream()
            .map(OrderItem::getSubTotal)
            .reduce(Money.ZERO, Money::add);
    }
}
