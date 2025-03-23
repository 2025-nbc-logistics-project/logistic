package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeignDeliveryRouteResponse {
    private UUID deliveryRouteId;
    private int sequence;
    private UUID departureHubId;
    private UUID destinationHubId;
    private Integer estimatedDistance;
    private Integer estimatedTime;
    private Integer actualDistance;
    private Integer actualTime;
    private String routeStatus;
    private UUID deliveryManagerId;
}
