package com.logistic.client.company.domain.exception.company;

import com.logistic.client.company.application.exception.CustomException;
import com.logistic.client.company.application.exception.ErrorCode;

public class CompanyValidationException extends CustomException {
    public CompanyValidationException() {
        super(ErrorCode.COMPANY_INVALID);
    }
}
