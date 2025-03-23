package com.logistic.client.order.domain.exception;

import com.logistic.client.order.application.exception.BusinessException;

public class OrderNotEditableException extends BusinessException {
    public OrderNotEditableException(String message) {
        super(message, "ORDER_NOT_EDITABLE", 400);
    }
}
