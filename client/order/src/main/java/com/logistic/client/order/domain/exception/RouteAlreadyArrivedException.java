package com.logistic.client.order.domain.exception;

import com.logistic.client.order.application.exception.BusinessException;

public class RouteAlreadyArrivedException extends BusinessException {
    public RouteAlreadyArrivedException(String message) {
        super(message, "ROUTE_ALREADY_ARRIVED", 400);
    }
}
