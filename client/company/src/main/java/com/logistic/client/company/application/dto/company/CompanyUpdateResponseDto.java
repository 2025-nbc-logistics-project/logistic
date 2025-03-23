package com.logistic.client.company.application.dto.company;

import com.logistic.client.company.domain.model.company.Company;
import com.logistic.client.company.domain.model.company.CompanyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyUpdateResponseDto {

    private UUID hubId;
    private CompanyType companyType;
    private String companyName;
    private String companyTel;
    private String postalCode;
    private String streetAddress;
    private String detailAddress;
    //담당자
    //private Long userId;

    public CompanyUpdateResponseDto(Company company) {
        this.hubId = company.getHubId();
        this.companyType = company.getCompanyType();
        this.companyName = company.getCompanyName();
        this.companyTel = company.getCompanyTel();
        this.postalCode = company.getAddress().getPostalCode();
        this.streetAddress = company.getAddress().getStreetAddress();
        this.detailAddress = company.getAddress().getDetailAddress();
    }
}
