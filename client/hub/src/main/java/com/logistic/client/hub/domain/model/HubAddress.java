package com.logistic.client.hub.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class HubAddress {

  private String postalCode;

  private String streetAddress;

  private String detailAddress;
}
