package com.logistic.client.hub.infrastructure.client;

import com.logistic.client.hub.application.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user", url = "http://localhost:18082")
public interface UserClient {
  @GetMapping("/api/v1/feign/users/{username}")
  UserResponseDto getUser(
      @RequestHeader("Authorization") String authorization,
      @RequestHeader("role") String role,
      @PathVariable("username") String username
  );
}