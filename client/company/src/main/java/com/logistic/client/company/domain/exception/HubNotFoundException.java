package com.logistic.client.company.domain.exception;

import com.logistic.client.company.application.exception.CustomException;
import com.logistic.client.company.application.exception.ErrorCode;

public class HubNotFoundException extends CustomException {
    public HubNotFoundException() {
        super(ErrorCode.HUB_NOT_FOUND);
    }
}
