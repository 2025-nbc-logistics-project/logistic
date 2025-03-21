package com.logistic.client.user.infrastructure.configuration.customException;

public class SamePasswordException extends RuntimeException {
    public SamePasswordException(String message) {
        super(message);
    }
}
