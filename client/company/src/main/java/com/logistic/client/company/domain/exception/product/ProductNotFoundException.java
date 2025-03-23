package com.logistic.client.company.domain.exception.product;

import com.logistic.client.company.application.exception.CustomException;
import com.logistic.client.company.application.exception.ErrorCode;

public class ProductNotFoundException extends CustomException {
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
