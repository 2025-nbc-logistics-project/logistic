package com.logistic.client.hub.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;
import lombok.Getter;
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class UserResponseDto {
  private String username;
  private String role;
}
