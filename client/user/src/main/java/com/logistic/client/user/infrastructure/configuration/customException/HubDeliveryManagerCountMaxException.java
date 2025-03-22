package com.logistic.client.user.infrastructure.configuration.customException;

public class HubDeliveryManagerCountMaxException extends RuntimeException {
    public HubDeliveryManagerCountMaxException(String message) {
        super(message);
    }
}
