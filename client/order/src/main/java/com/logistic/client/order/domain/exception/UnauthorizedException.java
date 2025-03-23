package com.logistic.client.order.domain.exception;

import com.logistic.client.order.application.exception.BusinessException;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED", 403);
    }
}
