package com.logistic.client.ai.application.service;

import com.logistic.client.ai.application.dto.*;
import com.logistic.client.ai.domain.model.Ai;
import com.logistic.client.ai.domain.repository.AiRepository;
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

    @Value("${GEMINI_URI}")
    private String geminiUrl;

    @Value("${GEMINI_KEY}")
    private String apiKey;

    private final AiRepository aiRepository;

    @Transactional
    public String createAi(AiRequestDto requestDto) {

        String text = String.format("1. 주문 요청 사항: %s%n"
                + "2. 발송지: %s%n"
                + "3. 경유지: %s%n"
                + "4. 도착지: %s%n"
                + "5. 배송 담당자 근무시간: 오전 9시부터 오후 6시까지 근무함.%n"
                + "발송지와 경유지에 있는 각 센터들의 이동 시간은 하루입니다. 그리고 경유지의 마지막 센터에서 도착지까지 이동 시간은 6시간 입니다.%n"
                + "위의 조건을 모두 고려해서 상품이 주문한 업체가 원하는 시간에 도착할 수 있도록 언제까지 발송해야 하는지,즉 최종 발송 시한을 계산해주는데 역산을 이용해서 계산해주세요. %n"
                + "그리고 마지막에 '위 내용을 기반으로 도출된 최종 발송 시한은 몇 월 며칠 몇 시 입니다'라는 문장을 추가해주세요.",
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
            String responseText = response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();
            String finalShippingDeadline = getFianlDeadling(responseText);

            Ai ai = new Ai(text, finalShippingDeadline);
            aiRepository.save(ai);

            return finalShippingDeadline;
        }

        return "최종 발송 시한을 계산할 수 없습니다.";
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

    public String getFianlDeadling(String responseText) {

        String keyword = "위 내용을 기반으로 도출된 최종 발송 시한은";

        if(responseText.contains(keyword)) {
            String result = responseText.substring(responseText.indexOf(keyword));
            result = result.replace("**", "");
            return result.trim();
        }

        return "최종 발송 시한을 계산할 수 없습니다.";
    }
}
