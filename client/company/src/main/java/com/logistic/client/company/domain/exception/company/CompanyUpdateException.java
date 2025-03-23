package com.logistic.client.company.domain.exception.company;

import com.logistic.client.company.application.exception.CustomException;
import com.logistic.client.company.application.exception.ErrorCode;

public class CompanyUpdateException extends CustomException {
    public CompanyUpdateException() {
        super(ErrorCode.COMPANY_UPDATE_ERROR);
    }
}
