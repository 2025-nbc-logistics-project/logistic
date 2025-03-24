package com.logistic.client.delivery.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RouteStepResponse {
    private Integer order;
    private UUID hubId;
    private String hubName;
    private double cumulativeDistanceKm;
    private double estimatedTimeHours;
}
