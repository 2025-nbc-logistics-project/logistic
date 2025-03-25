package com.logistic.client.company.infrastructure.client;

import com.logistic.client.company.application.dto.common.HubDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub")
public interface HubClient {

    @GetMapping("/api/v1/feign/hubs/{hubId}")
    HubDto getHub(@PathVariable("hubId") UUID hubId);

}
