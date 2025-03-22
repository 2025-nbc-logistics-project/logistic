package com.logistic.client.delivery.application.dto;

import com.logistic.client.delivery.domain.model.DeliveryHubInfo;
import com.logistic.client.delivery.domain.model.DeliveryRoute;
import com.logistic.client.delivery.domain.model.DeliveryRouteStatus;
import com.logistic.client.delivery.domain.model.DistanceTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DeliveryRouteDto {
    private UUID deliveryRouteId;
    private int sequence;
    private DeliveryHubInfo deliveryHubInfo;
    private DistanceTime estimated;
    private DistanceTime actual;
    private DeliveryRouteStatus routeStatus;
    private UUID deliveryManagerId;

    public DeliveryRouteDto(DeliveryRoute route) {
        this.deliveryRouteId = route.getDeliveryRouteId();
        this.sequence = route.getSequence();
        this.deliveryHubInfo = route.getDeliveryHubInfo();
        this.estimated = route.getEstimated();
        this.actual = route.getActual();
        this.routeStatus = route.getRouteStatus();
        this.deliveryManagerId = route.getDeliveryManagerId();
    }
}
