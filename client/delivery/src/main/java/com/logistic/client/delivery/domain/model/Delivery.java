package com.logistic.client.delivery.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "p_delivery")
@Where(clause = "is_deleted = false")
public class Delivery extends BaseEntity {
    @Id
    private UUID deliveryId;

    private UUID orderId;

    @Embedded
    private DeliveryManagerId deliveryManagerId;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Embedded
    private DeliveryHubInfo deliveryHubInfo;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "receiverAddress", column = @Column(name = "receiver_address")),
        @AttributeOverride(name = "detailAddress", column = @Column(name = "receiver_detail_address"))
    })
    private ShippingInfo shippingInfo;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

    public Delivery(UUID orderId, DeliveryManagerId deliveryManagerId, DeliveryHubInfo hubInfo, ShippingInfo shippingInfo) {
        this.deliveryId = UUID.randomUUID();
        this.orderId = orderId;
        this.deliveryManagerId = deliveryManagerId;
        this.status = DeliveryStatus.PENDING;
        this.deliveryHubInfo = hubInfo;
        this.shippingInfo = shippingInfo;
    }

    public void updateStatus(DeliveryStatus newStatus) { // 배송 상태 업데이트 메서드
        int currentSeq = this.status.getSequence();
        int newSeq = newStatus.getSequence();
        if (newSeq < currentSeq) {
            throw new IllegalArgumentException("잘못된 배송 상태(이전 상태)가 입력되었습니다.");
        }
        this.status = newStatus;
    }

    public void addRoute(DeliveryRoute route) { // 배송 경로 추가 메서드
        this.deliveryRoutes.add(route);
        route.setDelivery(this);
    }

    public void updateReceiverManager(UUID receiver) {
        UUID supplier = this.deliveryManagerId.getSupplierDeliveryManagerId();
        this.deliveryManagerId = new DeliveryManagerId(receiver, supplier);
    }

    public void updateSupplierManager(UUID supplier) {
        UUID receiver = this.deliveryManagerId.getReceiverDeliveryManagerId();
        this.deliveryManagerId = new DeliveryManagerId(receiver, supplier);
    }

    public void updateShippingInfo(ShippingInfo newShipping) {
        this.shippingInfo = newShipping;
    }

    @Override
    public void markAsDeleted(Long userId) {
        super.markAsDeleted(userId);

        if (this.deliveryRoutes != null) {
            for (DeliveryRoute route : this.deliveryRoutes) {
                route.markAsDeleted(userId);
            }
        }
    }
}
