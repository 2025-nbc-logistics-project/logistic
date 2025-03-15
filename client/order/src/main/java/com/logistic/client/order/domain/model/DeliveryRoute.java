package com.logistic.client.order.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeliveryRoute {
    @Id
    private UUID deliveryRouteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private int sequence;

    @Embedded
    private DeliveryHubInfo deliveryHubInfo;

    @Embedded
//    @AttributeOverrides({
//        @AttributeOverride(name = "distance", column = @Column(name = "estimated_distance")),
//        @AttributeOverride(name = "time", column = @Column(name = "estimated_time"))
//    })
    private DistanceTime estimated;
    @Embedded
    private DistanceTime actual;

    @Enumerated(EnumType.STRING)
    private DeliveryRouteStatus routeStatus;

    private UUID deliveryManagerId;

    public DeliveryRoute(int sequence, DeliveryHubInfo hubInfo, DistanceTime estimated, UUID deliveryManagerId) {
        this.deliveryRouteId = UUID.randomUUID();
        this.sequence = sequence;
        this.deliveryHubInfo = hubInfo;
        this.estimated = estimated;
        this.routeStatus = DeliveryRouteStatus.HUB_WAITING;
        this.deliveryManagerId =deliveryManagerId;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void updateActualDistanceTime(Double distance, Integer time) {
        this.actual = new DistanceTime(distance, time);
    }

    public void updateRouteStatus(DeliveryRouteStatus newStatus) {
        int currentSeq = this.routeStatus.getSequence();
        int newSeq = newStatus.getSequence();
        if (newSeq < currentSeq) {
            throw new IllegalArgumentException("잘못된 배송 상태(이전 상태)가 입력되었습니다.");
        }
        this.routeStatus = newStatus;
    }
}
