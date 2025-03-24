package com.logistic.client.company.application.dto.company;

import com.logistic.client.company.domain.model.company.Company;
import com.logistic.client.company.domain.model.company.CompanyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyCreateResponseDto {

    private UUID companyId;
    private UUID userId;
    private CompanyType companyType;
    private String companyName;
    private LocalDateTime createdAt;

    public CompanyCreateResponseDto(Company company) {
        this.companyId = company.getCompanyId();
        this.userId = company.getUserId();
        this.companyType = company.getCompanyType();
        this.companyName = company.getCompanyName();
    }
}
