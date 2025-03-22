package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private UUID companyId;
    private UUID hubId;
    private UUID deliveryManagerId;
    private AddressResponse address;
}
