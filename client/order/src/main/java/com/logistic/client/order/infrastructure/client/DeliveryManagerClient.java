package com.logistic.client.order.infrastructure.client;

import com.logistic.client.order.application.dto.DeliveryManagerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "users")
public interface DeliveryManagerClient {
    @GetMapping("/api/v1/delivery-managers/hub/{hubId}")
    DeliveryManagerResponse getDeliveryManagerIdByHubId(@PathVariable UUID hubId);
}
