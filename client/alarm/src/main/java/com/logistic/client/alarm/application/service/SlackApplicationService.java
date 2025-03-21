package com.logistic.client.alarm.application.service;

import com.logistic.client.alarm.application.dto.PageResponseDto;
import com.logistic.client.alarm.application.dto.SlackResponseDto;
import com.logistic.client.alarm.domain.model.Message;
import com.logistic.client.alarm.domain.model.Slack;
import com.logistic.client.alarm.domain.repository.SlackRepository;
import com.logistic.client.alarm.infrastructure.client.SlackApiClient;
import com.logistic.client.alarm.presentation.request.SlackRequestDto;
import com.logistic.client.alarm.presentation.request.SlackSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlackApplicationService {

    private final SlackRepository slackRepository;
    private final SlackApiClient slackApiClient;

    @Transactional
    public SlackResponseDto createSlackAndSend(SlackRequestDto requestDto) {
        // Slack 엔티티 생성 및 저장
        Slack slack = new Slack(
            requestDto.getReceiverSlackId(),
            1L, // TODO : 유저 Id를 받아와서 저장하기
            new Message(requestDto.getText())
        );

        slackRepository.save(slack);

        // DM 채널 열기
        String channelId = slackApiClient.openConversation(requestDto.getReceiverSlackId());

        // 메시지 전송
        slackApiClient.postMessage(channelId, requestDto.getText());

        return new SlackResponseDto(slack);
    }

    @Transactional(readOnly = true)
    public SlackResponseDto readSlack(UUID slackId) {
        Slack slack = findSlackById(slackId);

        return new SlackResponseDto(slack);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<SlackResponseDto> searchSlack(SlackSearchDto requestDto) {
        Page<SlackResponseDto> mappedPage = slackRepository.searchSlack(requestDto).map(SlackResponseDto::new);
        return new PageResponseDto<>(mappedPage);
    }

    @Transactional
    public void deleteSlack(UUID slackId) {
        Slack slack = findSlackById(slackId);

        slack.markAsDeleted(1L); // TODO : 유저 Id 추가
    }

    private Slack findSlackById(UUID slackId) {
        return slackRepository.findById(slackId)
            .orElseThrow(() -> new IllegalArgumentException("해당 Id를 가진 슬랙 메시지를 찾을 수 없습니다."));
    }
}
