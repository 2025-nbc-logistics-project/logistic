package com.logistic.client.company.domain.exception.common;

import com.logistic.client.company.application.exception.CustomException;
import com.logistic.client.company.application.exception.ErrorCode;

public class UnauthorizedAccessException extends CustomException {
    public UnauthorizedAccessException() {
        super(ErrorCode.USER_UNAUTHORIZED);
    }
}
