package com.logistic.client.ai.application.service;

import com.logistic.client.ai.application.dto.*;
import com.logistic.client.ai.domain.model.Ai;
import com.logistic.client.ai.domain.repository.AiRepository;
import com.logistic.client.ai.infrastructure.client.SlackClient;
import com.logistic.client.ai.presentation.request.AiRequestDto;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${gemini.uri}")
    private String geminiUrl;

    @Value("${gemini.key}")
    private String apiKey;

    private final AiRepository aiRepository;
    private final SlackClient slackClient;

    @Transactional
    public AiResponseDto createAi(AiRequestDto requestDto) {

        String text = String.format("1. 상품 및 수량정보: %s%n"
                + "2. 주문 요청 사항: %s%n"
                + "3. 발송지: %s%n"
                + "4. 경유지: %s%n"
                + "5. 도착지: %s%n"
                + "배송 담당자의 근무시간은 오전 9시부터 오후 6시까지야. %n"
                + "센터에서 센터까지 24시간 걸린다고 가정하고, 같은 도시 내의 센터에서 도착지까지는 6시간 걸린다고 가정하자.%n"
                + "이 데이터들을 모두 고려해서 상품이 주문한 업체가 원하는 시간에 도착할 수 있도록 언제까지 발송해야 하는지,즉 최종 발송 시한을 계산해줘. %n"
                + "그리고 대답은 '위 내용을 기반으로 도출된 최종 발송 시한은 몇 월 며칠 몇 시 입니다'라고만 대답해줘.",
                requestDto.getSlackOrderItems(),
                requestDto.getOrderRequest(),
                requestDto.getDepartureHubName(),
                requestDto.getTransitHubs(),
                requestDto.getDestinationAddress()
        );

        List<GeminiRequestDto.Part> parts = new ArrayList<>();
        parts.add(new GeminiRequestDto.Part(text));

        List<GeminiRequestDto.Content> contents = new ArrayList<>();
        contents.add(new GeminiRequestDto.Content(parts));

        GeminiRequestDto geminiRequestDto = new GeminiRequestDto(contents);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GeminiRequestDto> entity = new HttpEntity<>(geminiRequestDto, headers);
        String url = geminiUrl + "?key=" + apiKey;

        ResponseEntity<GeminiResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, GeminiResponseDto.class);

        if(response.getBody() != null
                && response.getBody().getCandidates() != null
                && !response.getBody().getCandidates().isEmpty()
                && response.getBody().getCandidates().get(0).getContent() != null
                && response.getBody().getCandidates().get(0).getContent().getParts() != null
                && !response.getBody().getCandidates().get(0).getContent().getParts().isEmpty()
                && response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText() != null
        ) {
            String finalShippingDeadline = response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();

            Ai ai = new Ai(text, finalShippingDeadline);
            aiRepository.save(ai);

            SlackRequestDto slackRequestDto = new SlackRequestDto(finalShippingDeadline);
            slackClient.createOrderSlack(slackRequestDto);

            return new AiResponseDto(finalShippingDeadline);
        }

        return new AiResponseDto("최종 발송 시한을 계산할 수 없습니다.");
    }

    public Page<AiListResponseDto> getAis(int page, int limit, String sortBy, String order, String role) {

        if(!("MASTER".equals(role))) {
            throw new SecurityException("권한이 없습니다.");
        }

        if(limit != 10 && limit != 30 && limit != 50) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Ai> ais = aiRepository.getAis(pageable, sortBy, order);
        return ais.map(AiListResponseDto::new);
    }
}
