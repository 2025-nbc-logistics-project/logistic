package com.logistic.client.delivery.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private String postalCode;
    private String detailAddress;
    private String streetAddress;
}
