package com.logistic.client.company.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "주문 상품 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    @Schema(description = "주문 상품 UUID", example = "e2eea1ab-463e-42e7-82d5-0da14235fe5a")
    private UUID productId;
    @Schema(description = "주문 개수", example = "10")
    private Integer quantity;
}
