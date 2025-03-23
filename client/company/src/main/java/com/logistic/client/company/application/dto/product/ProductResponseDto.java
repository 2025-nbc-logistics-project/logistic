package com.logistic.client.company.application.dto.product;

import com.logistic.client.company.domain.model.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductResponseDto {

    private UUID productId;
    private UUID companyId;
    private UUID hubId;
    private String productName;
    private int price;
    private int quantity;

    public ProductResponseDto(Product product) {
        this.productId = product.getProductId();
        this.companyId = product.getCompanyId();
        this.hubId = product.getHubId();
        this.productName = product.getProductInfo().getProductName();
        this.price = product.getProductInfo().getPrice();
        this.quantity = product.getQuantity().getQuantity();
    }
}
