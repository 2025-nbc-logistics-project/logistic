package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class DeliveryManagerId {
    private final UUID receiverDeliveryManagerId; // 수령 업체 배송 담당자 ID
    private final UUID supplierDeliveryManagerId; // 공급 업체 배송 담당자 ID

    protected DeliveryManagerId() {
        this.receiverDeliveryManagerId = null;
        this.supplierDeliveryManagerId = null;
    }

    public DeliveryManagerId(UUID receiver, UUID supplier) {
        if (receiver == null || supplier == null || receiver.equals(supplier)) {
            throw new IllegalArgumentException("잘못된 배송 담당자 Id 입니다.");
        }
        this.receiverDeliveryManagerId = receiver;
        this.supplierDeliveryManagerId = supplier;
    }
}
