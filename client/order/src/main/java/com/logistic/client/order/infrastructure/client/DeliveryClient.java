package com.logistic.client.order.infrastructure.client;

import com.logistic.client.order.application.dto.CreateDeliveryRequest;
import com.logistic.client.order.application.dto.FeignDeliveryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PostMapping("/api/v1/deliveries")
    FeignDeliveryResponse createDelivery(@RequestBody CreateDeliveryRequest requestDto);

    @PatchMapping("/api/v1/deliveries/{deliveryId}")
    void deleteDelivery(@PathVariable("deliveryId") UUID deliveryId);
}
