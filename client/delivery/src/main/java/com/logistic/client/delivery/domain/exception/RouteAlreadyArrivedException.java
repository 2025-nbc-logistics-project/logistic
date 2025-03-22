package com.logistic.client.delivery.domain.exception;


import com.logistic.client.delivery.application.exception.BusinessException;

public class RouteAlreadyArrivedException extends BusinessException {
    public RouteAlreadyArrivedException(String message) {
        super(message, "ROUTE_ALREADY_ARRIVED", 400);
    }
}
