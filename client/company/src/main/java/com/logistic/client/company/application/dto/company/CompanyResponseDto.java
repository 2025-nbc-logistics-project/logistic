package com.logistic.client.company.application.dto.company;

import com.logistic.client.company.domain.model.company.Company;
import com.logistic.client.company.domain.model.company.CompanyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyResponseDto {

    private UUID companyId;
    private UUID hubId;
    private UUID userId;
    private CompanyType companyType;
    private String companyName;
    private String companyTel;
    private String postalCode;
    private String streetAddress;
    private String detailAddress;

    public CompanyResponseDto(Company company) {
        this.companyId = company.getCompanyId();
        this.hubId = company.getHubId();
        this.userId = company.getUserId();
        this.companyType = company.getCompanyType();
        this.companyName = company.getCompanyName();
        this.companyTel = company.getCompanyTel();
        this.postalCode = company.getAddress().getPostalCode();
        this.streetAddress = company.getAddress().getStreetAddress();
        this.detailAddress = company.getAddress().getDetailAddress();
    }

}
