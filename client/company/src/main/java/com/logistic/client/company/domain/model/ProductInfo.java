package com.logistic.client.company.domain.model;

import com.logistic.client.company.domain.exception.product.ProductValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInfo {

    @Column(name = "product_name", nullable = false)
    private String productName;

    private Integer price;

    public ProductInfo(String productName, Integer price) {
        if(productName == null) {
            throw new ProductValidationException();
        }
        if (price == null || price < 0) {
            throw new ProductValidationException();
        }
        this.productName = productName;
        this.price = price;

    }
}
