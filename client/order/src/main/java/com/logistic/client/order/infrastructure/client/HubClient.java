package com.logistic.client.order.infrastructure.client;

import com.logistic.client.order.application.dto.HubRouteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/api/hubs")
    List<HubRouteResponse> getHubRoutes(@RequestParam("departureHubId") UUID departureHubId,
                                        @RequestParam("destinationHubId") UUID destinationHubId);
}
