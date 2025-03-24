package com.logistic.client.ai.infrastructure.client;

import com.logistic.client.ai.application.dto.SlackRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slack")
public interface SlackClient {

    //슬랙 전송
    @PostMapping("/api/v1/slack/order")
    void createOrderSlack(@RequestBody SlackRequestDto requestDto);
}
