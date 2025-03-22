package com.logistic.client.hub.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouteStepResponse {

  private UUID hubId;
  private String hubName;
  private int cumulativeDistance;
}