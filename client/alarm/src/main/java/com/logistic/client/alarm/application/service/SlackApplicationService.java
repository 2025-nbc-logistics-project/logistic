package com.logistic.client.alarm.application.service;

import com.logistic.client.alarm.application.dto.*;
import com.logistic.client.alarm.domain.model.Message;
import com.logistic.client.alarm.domain.model.Slack;
import com.logistic.client.alarm.domain.repository.SlackRepository;
import com.logistic.client.alarm.infrastructure.client.AIClient;
import com.logistic.client.alarm.infrastructure.client.HubClient;
import com.logistic.client.alarm.infrastructure.client.SlackApiClient;
import com.logistic.client.alarm.infrastructure.client.UserClient;
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
    private final UserClient userClient;
    private final AIClient aiClient;
    private final HubClient hubClient;

    @Transactional
    public void createOrderSlack(OrderInfoDto requestDto) {
        // 유저 서비스를 호출하여 발송지 허브 담당자의 슬랙 Id 조회
        String hubManagerSlackId = userClient.getHubManagerSlackId(requestDto.getDepartureHubId());

        // 허브 서비스를 호출하여 경유 허브 Id를 통해 허브 이름 리스트를 반환 받음
        TransitHubResponse transitHubNames = hubClient.getHubNames(new TransitHubRequest(requestDto.getTransitHubs()));

        // AI 서비스를 호출하여 주문 정보를 토대로 AI 응답을 반환 받음
        AiRequestDto aiRequestDto = new AiRequestDto(requestDto, transitHubNames.getHubNames());
        String aiResponse = aiClient.createSlackMsg(aiRequestDto);

        // 전달 형식에 맞게 메시지 가공
        String finalMessage = buildSlackMessage(requestDto, transitHubNames, aiResponse);

        // Slack 엔티티 생성 및 저장
        Slack slack = new Slack(
            hubManagerSlackId,
            null, // 자동 생성되는 슬랙 메시지라 userId는 null 로 설정
            new Message(finalMessage)
        );
        slackRepository.save(slack);

        // DM 채널 열기
        String channelId = slackApiClient.openConversation(hubManagerSlackId);

        // 메시지 전송
        slackApiClient.postMessage(channelId, finalMessage);
    }

    @Transactional
    public SlackResponseDto createSlackAndSend(SlackRequestDto requestDto, UUID userID) {
        // Slack 엔티티 생성 및 저장
        Slack slack = new Slack(
            requestDto.getReceiverSlackId(),
            userID,
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

    private String buildSlackMessage(OrderInfoDto dto, TransitHubResponse hubNames, String aiResponse) {
        // (1). 상품 목록을 문자열로 만들기
        StringBuilder productsStr = new StringBuilder();
        for (ProductNameQuantity item : dto.getSlackOrderItems()) {
            productsStr.append(String.format("%s x %d개\n", item.getProductName(), item.getQuantity()));
        }

        // (2). 경유 허브 목록 문자열로 만들기
        StringBuilder transitStr = new StringBuilder();
        if (!hubNames.getHubNames().isEmpty()) {
            for (String hubName : hubNames.getHubNames()) {
                transitStr.append(hubName).append(", ");
            }
            if (transitStr.length() >= 2) {
                transitStr.setLength(transitStr.length() - 2);
            }
        } else {
            transitStr.append("경유지 없음");
        }

        // (3). 도착지 주소 + 업체명
        AddressResponse addr = dto.getDestinationAddress();
        String fullAddress = String.format("%s %s %s %s",
            addr.getPostalCode(), addr.getStreetAddress(), addr.getDetailAddress(), dto.getReceiverCompanyName());

        // (4) 최종 문자열 조합
        return String.format(
            """
                주문 번호 : %s
                주문자명 : %s
                상품 정보 :\s
                %s요청 사항 : %s
                발송지 : %s
                경유지 : %s
                도착지 : %s
                배송담당자 : %s

                %s
                """,
            dto.getOrderId(),
            dto.getUsername(),
            productsStr.toString(),
            dto.getOrderRequest(),
            dto.getDepartureHubId(),
            transitStr.toString(),
            fullAddress,
            dto.getDeliveryManagerName(),
            aiResponse
        );
    }
}
