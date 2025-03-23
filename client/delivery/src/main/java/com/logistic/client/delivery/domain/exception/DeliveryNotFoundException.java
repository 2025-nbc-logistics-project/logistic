package com.logistic.client.delivery.domain.exception;


import com.logistic.client.delivery.application.exception.BusinessException;

public class DeliveryNotFoundException extends BusinessException {
    public DeliveryNotFoundException(String message) {
        super(message, "DELIVERY_NOT_FOUND", 404);
    }
}
