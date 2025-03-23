package com.logistic.client.company.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub")
public interface HubClient {

    // 허브 존재 확인 메소드
    @GetMapping("/api/v1/hubs/{hudId}")
    boolean existsHub(@PathVariable("hudId") UUID hudId);

}
