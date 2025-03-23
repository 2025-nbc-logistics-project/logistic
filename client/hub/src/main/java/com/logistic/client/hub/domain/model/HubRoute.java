package com.logistic.client.hub.domain.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@Table(name = "p_hub_route")
public class HubRoute extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "departHubId", column = @Column(name = "depart_hub_id", nullable = false)),
      @AttributeOverride(name = "arriveHubId", column = @Column(name = "arrive_hub_id", nullable = false))
  })
  private HubRouteId hubRouteId;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "estimatedTime", column = @Column(name = "estimated_time")),
      @AttributeOverride(name = "distance", column = @Column(name = "distance", nullable = false))
  })
  private HubRouteInfo hubRouteInfo;

}
