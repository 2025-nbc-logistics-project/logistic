package com.logistic.client.delivery.domain.exception;


import com.logistic.client.delivery.application.exception.BusinessException;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED", 403);
    }
}
