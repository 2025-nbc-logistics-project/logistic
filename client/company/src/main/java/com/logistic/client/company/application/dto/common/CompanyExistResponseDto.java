package com.logistic.client.company.application.dto.common;

import com.logistic.client.company.domain.model.company.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyExistResponseDto {

    private UUID companyId;
    private UUID userId;

    public CompanyExistResponseDto(Company company) {
        this.companyId = company.getCompanyId();
        this.userId = company.getUserId();
    }

}
