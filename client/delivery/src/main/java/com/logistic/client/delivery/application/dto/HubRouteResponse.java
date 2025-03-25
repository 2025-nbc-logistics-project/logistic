package com.logistic.client.delivery.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteResponse {
    private Integer sequence;
    private UUID departureHubId;
    private UUID destinationHubId;
    private UUID deliveryManagerId;
    private double distance;
    private double time;
}
