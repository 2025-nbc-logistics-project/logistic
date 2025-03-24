package com.logistic.client.delivery.application.dto;

import com.logistic.client.delivery.domain.model.DeliveryRoute;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class FeignDeliveryRouteResponse {
    private UUID deliveryRouteId;
    private Integer sequence;
    private UUID departureHubId;
    private UUID destinationHubId;
    private double estimatedDistance;
    private double estimatedTime;
    private double actualDistance;
    private double actualTime;
    private String routeStatus;
    private UUID deliveryManagerId;

    public FeignDeliveryRouteResponse(DeliveryRoute route) {
        this.deliveryRouteId = route.getDeliveryRouteId();
        this.sequence = route.getSequence();
        this.departureHubId = route.getDeliveryHubInfo().getDepartureHubId();
        this.destinationHubId = route.getDeliveryHubInfo().getDestinationHubId();
        this.estimatedDistance = route.getEstimated().getDistance();
        this.estimatedTime = route.getEstimated().getTime();
        this.actualDistance = 0;
        this.actualTime = 0;
        this.routeStatus = route.getRouteStatus().name();
        this.deliveryManagerId = route.getDeliveryManagerId();
    }
}
