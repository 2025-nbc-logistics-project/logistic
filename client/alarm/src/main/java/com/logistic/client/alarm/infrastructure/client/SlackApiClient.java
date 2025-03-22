package com.logistic.client.alarm.infrastructure.client;

import com.logistic.client.alarm.application.dto.OpenConversationRequest;
import com.logistic.client.alarm.application.dto.OpenConversationResponse;
import com.logistic.client.alarm.application.dto.PostMessageRequest;
import com.logistic.client.alarm.application.dto.PostMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class SlackApiClient {

    private final SlackFeignClient slackFeignClient;

    @Value("${slack.bot-token}")
    private String botToken;

    public String openConversation(String slackId) {
        // (1). Request 생성
        OpenConversationRequest request = new OpenConversationRequest(Collections.singletonList(slackId));

        // (2). Feign 호출
        OpenConversationResponse response = slackFeignClient.openConversation(
            "Bearer " + botToken, request
        );

        // (3). 응답 처리
        if (!response.isOk()) {
            throw new RuntimeException("Slack 채널 생성에 실패하였습니다. Error : " + response.getError());
        }

        // (4). DM 채널 Id 반환
        return response.getChannel().getId();
    }

    public void postMessage(String channelId, String text) {
        // (1). Request 생성
        PostMessageRequest request = new PostMessageRequest(channelId, text);

        // (2). Feign 호출
        PostMessageResponse response = slackFeignClient.postMessage(
            "Bearer " + botToken, request
        );

        // (3). 응답 처리
        if (!response.isOk()) {
            throw new RuntimeException("Slack 메시지 전송에 실패하였습니다. Error : " + response.getError());
        }
    }
}
