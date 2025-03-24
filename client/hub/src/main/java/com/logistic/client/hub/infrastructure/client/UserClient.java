package com.logistic.client.hub.infrastructure.client;

import com.logistic.client.hub.application.dto.UserResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClient {
  @GetMapping("/api/v1/users/{username}")
  UserResponseDto getUser(
      @RequestHeader("Authorization") String authorization,
      @RequestHeader("role") String role,
      @PathVariable("username") String username
  );
}