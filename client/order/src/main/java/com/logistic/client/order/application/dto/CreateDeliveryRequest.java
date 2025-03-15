package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
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
