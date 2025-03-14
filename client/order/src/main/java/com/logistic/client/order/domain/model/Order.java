package com.logistic.client.order.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class Order {
    @Id
    private UUID orderId;

    @Embedded
    private CompanyInfo companyInfo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Embedded
    private Money totalPrice;

    private String orderRequest;


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
