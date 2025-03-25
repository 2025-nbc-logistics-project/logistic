package com.logistic.client.company.infrastructure.client;

import com.logistic.client.company.application.dto.common.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClient {

    @GetMapping("/api/v1/feign/users/{username}")
    UserDto getHubId(@PathVariable("username") String username);
}
