package com.logistic.client.company.domain.exception.common;

import com.logistic.client.company.application.exception.CustomException;
import com.logistic.client.company.application.exception.ErrorCode;

public class AlreadyDeletedException extends CustomException {
    public AlreadyDeletedException() {
        super(ErrorCode.HUB_NOT_FOUND);
    }
}
