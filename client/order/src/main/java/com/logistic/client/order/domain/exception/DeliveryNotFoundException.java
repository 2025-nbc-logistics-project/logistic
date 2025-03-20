package com.logistic.client.order.domain.exception;

import com.logistic.client.order.application.exception.BusinessException;

public class DeliveryNotFoundException extends BusinessException {
    public DeliveryNotFoundException(String message) {
        super(message, "DELIVERY_NOT_FOUND", 404);
    }
}
