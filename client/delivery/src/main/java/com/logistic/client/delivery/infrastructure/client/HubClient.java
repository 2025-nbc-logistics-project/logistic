package com.logistic.client.delivery.infrastructure.client;

import com.logistic.client.delivery.application.dto.FindRouteResponse;
import com.logistic.client.delivery.application.dto.HubRouteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub")
public interface HubClient {

    @GetMapping("/api/v1/hubs/feign/routes")
    FindRouteResponse getHubRoutes(@RequestParam("departHubId") UUID departureHubId,
                                   @RequestParam("arriveHubId") UUID destinationHubId);
}
