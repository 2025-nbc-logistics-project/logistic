package com.logistic.client.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "slack-service")
public interface SlackClient {
}
