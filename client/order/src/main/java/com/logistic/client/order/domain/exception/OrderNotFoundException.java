package com.logistic.client.order.domain.exception;

import com.logistic.client.order.application.exception.BusinessException;

public class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException(String message) {
        super(message, "ORDER_NOT_FOUND", 404);
    }
}
