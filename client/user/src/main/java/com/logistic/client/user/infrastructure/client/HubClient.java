package com.logistic.client.user.infrastructure.client;

import com.logistic.client.user.application.dto.responseDto.HubResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub")
public interface HubClient {
    @GetMapping("/api/v1/hubs/{hubId}")
    HubResDTO getHubById(@PathVariable("hubId") UUID hubId);
}
