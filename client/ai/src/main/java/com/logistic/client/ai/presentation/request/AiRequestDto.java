package com.logistic.client.ai.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "AI 메시지 생성 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AiRequestDto {

    @Schema(description = "주문 요청 사항", example = "12월 12일 3시까지는 보내주세요!")
    private String orderRequest;
    @Schema(description = "출발 허브 이름", example = "경기 북부 센터")
    private String departureHubName;
    @Schema(description = "경유 허브명 리스트", example = "[\"대전광역시 센터\", \"부산광역시 센터\"]")
    private List<String> transitHubs;
    @Schema(description = "수령 업체 주소", example = "{\n" +
            "  \"postalCode\": \"12345\",\n" +
            "  \"streetAddress\": \"부산시 사하구 낙동대로 1번길 1\",\n" +
            "  \"detailAddress\": \"해산물월드\"\n" +
            "}")
    private AddressResponse destinationAddress;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressResponse {
        private String postalCode;
        private String detailAddress;
        private String streetAddress;
    }
}
