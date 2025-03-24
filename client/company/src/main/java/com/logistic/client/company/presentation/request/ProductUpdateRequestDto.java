package com.logistic.client.company.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "상품 수정 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequestDto {

    @Schema(description = "소속 업체 UUID", example = "e2eea1ab-463e-42e7-82d5-0da14235fe5a")
    private UUID hubId;
    @Schema(description = "상품 이름", example = "볼펜")
    private String productName;
    @Schema(description = "상품 가격", example = "1000")
    private Integer price;
    @Schema(description = "상품 개수", example = "10")
    private Integer quantity;

}
