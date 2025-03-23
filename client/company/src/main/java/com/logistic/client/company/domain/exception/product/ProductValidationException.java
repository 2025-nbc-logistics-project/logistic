package com.logistic.client.company.domain.exception.product;

import com.logistic.client.company.application.exception.CustomException;
import com.logistic.client.company.application.exception.ErrorCode;

public class ProductValidationException extends CustomException {
    public ProductValidationException() {
        super(ErrorCode.PRODUCT_INVALID);
    }
}
