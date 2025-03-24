package com.logistic.client.ai.application.dto;

import com.logistic.client.ai.domain.model.Ai;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiListResponseDto {
    private String request_text;
    private String response_text;

    public AiListResponseDto(Ai ai) {
        this.request_text = ai.getRequest_text();
        this.response_text = ai.getResponse_text();
    }
}
