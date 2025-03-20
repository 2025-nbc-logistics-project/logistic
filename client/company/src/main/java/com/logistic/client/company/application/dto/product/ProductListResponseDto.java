package com.logistic.client.company.application.dto.product;

import com.logistic.client.company.domain.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductListResponseDto {

    private UUID productId;
    private UUID companyId;
    private String productName;

    public ProductListResponseDto(Product product) {
       this.productId = product.getProductId();
       this.companyId = product.getCompanyId();
       this.productName = product.getProductInfo().getProductName();

    }

}
