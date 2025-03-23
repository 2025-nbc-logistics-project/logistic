package com.logistic.client.company.application.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductPriceResponseDto {

    private UUID productId;
    private String productName;
    private Integer price;

    public ProductPriceResponseDto(UUID productId, String productName, int price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }
}
