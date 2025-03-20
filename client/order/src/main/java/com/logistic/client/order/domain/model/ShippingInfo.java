package com.logistic.client.order.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class ShippingInfo {
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "postalCode", column = @Column(name = "supplier_postal_code")),
        @AttributeOverride(name = "detailAddress", column = @Column(name = "supplier_detail_address")),
        @AttributeOverride(name = "streetAddress", column = @Column(name = "supplier_street_address"))
    })
    private final Address supplierAddress; // 수령 업체 주소

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "postalCode", column = @Column(name = "receiver_postal_code")),
        @AttributeOverride(name = "detailAddress", column = @Column(name = "receiver_detail_address")),
        @AttributeOverride(name = "streetAddress", column = @Column(name = "receiver_street_address"))
    })
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
