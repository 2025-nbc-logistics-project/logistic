package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteResponse {
    private int sequence;
    private UUID departureHubId;
    private UUID destinationHubId;
    private UUID deliveryManagerId;
    private Integer distance;
    private Integer time;
}
