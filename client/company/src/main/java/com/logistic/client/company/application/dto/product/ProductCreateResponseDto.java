package com.logistic.client.company.application.dto.product;

import com.logistic.client.company.domain.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductCreateResponseDto {

    private UUID productId;
    private UUID companyId;
    private UUID hudId;
    private String productName;
    //
    private int price;
    private int quantity;

    public ProductCreateResponseDto(Product product) {
        this.productId = product.getProductId();
        this.companyId = product.getCompanyId();
        this.hudId = product.getHubId();
        this.productName = product.getProductInfo().getProductName();
        //
        this.price = product.getProductInfo().getPrice();
        this.quantity = product.getQuantity().getQuantity();
    }
}
