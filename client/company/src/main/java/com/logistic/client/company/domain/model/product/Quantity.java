package com.logistic.client.company.domain.model.product;

import com.logistic.client.company.domain.exception.product.ProductValidationException;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {

    private Integer quantity;

    public Quantity(Integer quantity) {
        if(quantity == null || quantity < 0) {
            throw new ProductValidationException();
        }
        this.quantity = quantity;
    }

}
