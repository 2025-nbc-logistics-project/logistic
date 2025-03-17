package com.logistic.client.hub.domain.model;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.Getter;

@Getter
@Embeddable
public class HubRouteId {

  private UUID departHubId;

  private UUID arriveHubId;
}
