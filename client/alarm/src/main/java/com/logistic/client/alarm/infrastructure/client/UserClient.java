package com.logistic.client.alarm.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "users")
public interface UserClient {
    @GetMapping("/api/v1/users/feign/hub/{hubId}")
    String getHubManagerSlackId(@PathVariable("hubId") UUID hubId);

}
