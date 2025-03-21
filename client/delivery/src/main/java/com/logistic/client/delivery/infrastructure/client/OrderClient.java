package com.logistic.client.delivery.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "order-service")
public interface OrderClient {

    @PatchMapping("/api/v1/orders/status/{orderId}")
    void updateOrderStatus(@PathVariable("orderId") UUID orderId,
                           @RequestParam("status") String status);
}
