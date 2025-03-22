package com.logistic.client.hub.domain.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Table(name = "p_hub_route")
public class HubRoute extends BaseEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Embedded
  private HubRouteId hubRouteId;
  @Embedded
  private HubRouteInfo hubRouteInfo;

}
