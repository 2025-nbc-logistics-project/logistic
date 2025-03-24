package com.logistic.client.hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class FindRouteResponse {

  private List<RouteStepResponse> route;
  private double totalDistanceKm;
  private double totalEstimatedTimeHours;
}