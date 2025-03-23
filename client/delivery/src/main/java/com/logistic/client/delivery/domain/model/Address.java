package com.logistic.client.delivery.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private final String postalCode;
    private final String detailAddress;
    private final String streetAddress;

    protected Address() {
        this.postalCode = null;
        this.detailAddress = null;
        this.streetAddress = null;
    }

    public Address(String postalCode, String detailAddress, String streetAddress) {
        this.postalCode = postalCode;
        this.detailAddress = detailAddress;
        this.streetAddress = streetAddress;
    }
}
