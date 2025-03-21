package com.logistic.client.company.application.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductPriceResponseDto {

    private UUID productId;
    private Integer price;

    public ProductPriceResponseDto(UUID productId, int price) {
        this.productId = productId;
        this.price = price;
    }
}
