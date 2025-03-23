package com.logistic.client.hub.application.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class UserResponseDto {
  private UUID userId;
  private String role;
}
