package com.logistic.client.company.domain.exception.company;

import com.logistic.client.company.application.exception.CustomException;
import com.logistic.client.company.application.exception.ErrorCode;

public class CompanyNotFoundException extends CustomException {
    public CompanyNotFoundException() {
        super(ErrorCode.COMPANY_NOT_FOUND);
    }
}
