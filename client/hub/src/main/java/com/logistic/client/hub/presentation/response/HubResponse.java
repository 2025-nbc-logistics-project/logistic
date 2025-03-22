package com.logistic.client.hub.presentation.response;

import java.util.UUID;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class HubResponse {

  private UUID id;
  private String name;
  private String postalCode;
  private String streetAddress;
  private String detailAddress;
  private Double latitude;
  private Double longitude;
}