package com.logistic.client.hub.presentation.request;

import lombok.Getter;

@Getter
public class CreateHubRequest {

  private String name;
  private String postalCode;
  private String streetAddress;
  private String detailAddress;
  private Integer latitude;
  private Integer longitude;
}