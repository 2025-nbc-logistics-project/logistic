package com.logistic.client.user.infrastructure.configuration.customException;

public class NotDeliveryManagerException extends RuntimeException {
    public NotDeliveryManagerException(String message) {
        super(message);
    }
}
