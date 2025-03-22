package com.logistic.client.delivery.application.dto;

import com.logistic.client.delivery.domain.model.DeliveryRoute;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
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

    public FeignDeliveryRouteResponse(DeliveryRoute route) {
        this.deliveryRouteId = route.getDeliveryRouteId();
        this.sequence = route.getSequence();
        this.departureHubId = route.getDeliveryHubInfo().getDepartureHubId();
        this.destinationHubId = route.getDeliveryHubInfo().getDestinationHubId();
        this.estimatedDistance = route.getEstimated().getDistance();
        this.estimatedTime = route.getEstimated().getTime();
        this.actualDistance = route.getActual().getDistance();
        this.actualTime = route.getActual().getTime();
        this.routeStatus = route.getRouteStatus().name();
        this.deliveryManagerId = route.getDeliveryManagerId();
    }
}
