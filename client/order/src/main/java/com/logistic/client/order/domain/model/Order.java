package com.logistic.client.order.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "p_order")
public class Order extends BaseEntity {
    @Id
    private UUID orderId;

    private UUID deliveryId;

//    private Orderer orderer;

    @Embedded
    private CompanyInfo companyInfo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Embedded
    private Money totalPrice;

    private String orderRequest;

    private OrderStatus orderStatus;


    public Order(CompanyInfo companyInfo, List<OrderItem> orderItems, String orderRequest) {
        this.orderId = UUID.randomUUID();
        this.companyInfo = companyInfo;
        this.orderItems = orderItems;
        this.orderRequest = orderRequest;
        this.orderStatus = OrderStatus.PENDING;
        this.totalPrice = orderItems.stream() // 총 합계 계산
            .map(OrderItem::getSubTotal)
            .reduce(Money.ZERO, Money::add);
    }

    public void updateStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }

    public void updateItems(List<OrderItem> newOrderItems) {
        this.orderItems.clear();
        this.orderItems.addAll(newOrderItems);
        this.totalPrice = orderItems.stream()
            .map(OrderItem::getSubTotal)
            .reduce(Money.ZERO, Money::add);
    }

    public void updateRequest(String orderRequest) {
        this.orderRequest = orderRequest;
    }

    public void checkUpdateAvailable() {
        if (!this.orderStatus.equals(OrderStatus.PENDING)) {
            throw new IllegalArgumentException("이미 배송이 진행 중이므로 주문 정보를 수정할 수 없습니다.");
        }
    }

    public void addDelivery(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }
}
