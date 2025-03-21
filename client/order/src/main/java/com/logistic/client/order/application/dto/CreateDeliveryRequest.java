package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryRequest {
    private UUID orderId;
    private UUID departureHubId;
    private UUID supplierDeliveryManager;
    private AddressResponse supplierAddress;
    private UUID destinationHubId;
    private UUID receiverDeliveryManager;
    private AddressResponse receiverAddress;
}
