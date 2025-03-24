package com.logistic.client.company.application.dto.company;

import com.logistic.client.company.domain.model.company.Company;
import com.logistic.client.company.domain.model.company.CompanyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyListResponseDto {

    private UUID companyId;
    private UUID hubId;
    private CompanyType companyType;
    private String companyName;

    public CompanyListResponseDto(Company company) {
        this.companyId = company.getCompanyId();
        this.hubId = company.getHubId();
        this.companyType = company.getCompanyType();
        this.companyName = company.getCompanyName();
    }

}
