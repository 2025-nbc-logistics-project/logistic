package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class ShippingInfo {
    private final Address supplierAddress; // 수령 업체 주소
    private final Address receiverAddress; // 공급 업체 주소
    private final String recipientName; // 수령인
    private final UUID recipientSlackId; // 수령인 슬랙 ID

    protected ShippingInfo() {
        this.supplierAddress = null;
        this.receiverAddress = null;
        this.recipientName = null;
        this.recipientSlackId = null;
    }

    public ShippingInfo(Address supplierAddress, Address receiverAddress, String recipientName, UUID recipientSlackId) {
        this.supplierAddress = supplierAddress;
        this.receiverAddress = receiverAddress;
        this.recipientName = recipientName;
        this.recipientSlackId = recipientSlackId;
    }
}
