package com.logistic.client.company.application.dto.product;

import com.logistic.client.company.domain.model.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductDeleteResponseDto {

    private UUID productId;
    private LocalDateTime deletedAt;
    private UUID deletedBy;

    public ProductDeleteResponseDto(Product product) {
        this.productId = product.getProductId();
        this.deletedAt = product.getDeletedAt();
        this.deletedBy = product.getDeletedBy();
    }

}
