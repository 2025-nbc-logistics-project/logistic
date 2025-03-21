package com.logistic.client.user.infrastructure.configuration.customException;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }
}
