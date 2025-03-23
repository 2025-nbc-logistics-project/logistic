package com.logistic.client.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "delivery-manager-service")
public interface DeliveryManagerClient {
    @GetMapping("/api/v1/delivery-managers/hub/{hubId}")
    UUID getDeliveryManagerIdByHubId(@PathVariable UUID hubId);
}
