package com.logistic.client.alarm.infrastructure.client;

import com.logistic.client.alarm.application.dto.OpenConversationRequest;
import com.logistic.client.alarm.application.dto.OpenConversationResponse;
import com.logistic.client.alarm.application.dto.PostMessageRequest;
import com.logistic.client.alarm.application.dto.PostMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "SlackFeignClient",
    url = "${slack.api-url}"
)
public interface SlackFeignClient {

    // DM 채널 열기
    @PostMapping("/conversations.open")
    OpenConversationResponse openConversation(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody OpenConversationRequest request);

    @PostMapping("/chat.postMessage")
    PostMessageResponse postMessage(@RequestHeader("Authorization") String authorizationHeader,
                                    @RequestBody PostMessageRequest request);
}
