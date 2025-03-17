package com.logistic.client.hub.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class HubRouteInfo {

  private Integer estimatedTime;
  private Integer distance;
}
