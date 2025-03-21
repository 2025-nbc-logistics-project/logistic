package com.logistic.client.order.application.dto;

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
