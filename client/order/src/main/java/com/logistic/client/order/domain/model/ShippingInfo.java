package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class ShippingInfo {
    private final Address deliveryAddress;
    private final String recipientName;
    private final UUID recipientSlackId;

    protected ShippingInfo() {
        this.deliveryAddress = null;
        this.recipientName = null;
        this.recipientSlackId = null;
    }

    public ShippingInfo(Address deliveryAddress, String recipientName, UUID recipientSlackId) {
        this.deliveryAddress = deliveryAddress;
        this.recipientName = recipientName;
        this.recipientSlackId = recipientSlackId;
    }
}
