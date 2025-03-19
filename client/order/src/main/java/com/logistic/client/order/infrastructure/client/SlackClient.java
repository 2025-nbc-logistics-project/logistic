package com.logistic.client.order.infrastructure.client;

import com.logistic.client.order.application.dto.SlackRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slack-service")
public interface SlackClient {

    @PostMapping("/api/slack")
    void createSlackMessage(@RequestBody SlackRequestDto requestDto);
}
