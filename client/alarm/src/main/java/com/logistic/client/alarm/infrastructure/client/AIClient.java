package com.logistic.client.alarm.infrastructure.client;

import com.logistic.client.alarm.application.dto.AiRequestDto;
import com.logistic.client.alarm.application.dto.OrderInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai")
public interface AIClient {
    @PostMapping("/api/v1/ai")
    String createSlackMsg(@RequestBody AiRequestDto request);
}
