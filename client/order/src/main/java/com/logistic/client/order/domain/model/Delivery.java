package com.logistic.client.order.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Delivery {
    @Id
    private UUID deliveryId;

    private UUID orderId;
    private UUID deliveryManagerId;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Embedded
    private DeliveryHubInfo deliveryHubInfo;

    @Embedded
    private ShippingInfo shippingInfo;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

    public Delivery(UUID orderId, UUID deliveryManagerId, DeliveryHubInfo hubInfo, ShippingInfo shippingInfo) {
        this.deliveryId = UUID.randomUUID();
        this.orderId = orderId;
        this.deliveryManagerId = deliveryManagerId;
        this.deliveryHubInfo = hubInfo;
        this.shippingInfo = shippingInfo;
    }

    public void updateStatus(DeliveryStatus newStatus) {
        int currentSeq = this.status.getSequence();
        int newSeq = newStatus.getSequence();
        if (newSeq < currentSeq) {
            throw new IllegalArgumentException("잘못된 배송 상태(이전 상태)가 입력되었습니다.");
        }
        this.status = newStatus;
    }

    public void addRoute(DeliveryRoute route) {
        this.deliveryRoutes.add(route);
        route.setDelivery(this);
    }
}
