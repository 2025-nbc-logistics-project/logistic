package com.logistic.client.alarm.infrastructure.client;

import com.logistic.client.alarm.application.dto.TransitHubRequest;
import com.logistic.client.alarm.application.dto.TransitHubResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/api/v1/hubs/name")
    TransitHubResponse getHubNames(@RequestBody TransitHubRequest transitHubs);
}
