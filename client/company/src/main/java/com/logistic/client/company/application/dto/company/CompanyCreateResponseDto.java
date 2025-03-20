package com.logistic.client.company.application.dto.company;

import com.logistic.client.company.domain.model.Company;
import com.logistic.client.company.domain.model.CompanyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyCreateResponseDto {

    private UUID companyId;
    private CompanyType companyType;
    private String companyName;
    //
//    private LocalDateTime createdAt;

    public CompanyCreateResponseDto(Company company) {
        this.companyId = company.getCompanyId();
        this.companyType = company.getCompanyType();
        this.companyName = company.getCompanyName();
//        this.createdAt = company.getCreatedAt;
    }
}
