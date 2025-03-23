package com.logistic.client.alarm.infrastructure.client;

import com.logistic.client.alarm.application.dto.OrderInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
public interface AIClient {
    @PostMapping("/api/v1/ai/slack")
    String createSlackMsg(@RequestBody OrderInfoDto request);
}
