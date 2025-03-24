package com.logistic.client.company.application.dto;

import com.logistic.client.company.domain.model.company.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeignCompanyResponse {
    private UUID companyId;
    private String companyName;
    private UUID hubId;
    private AddressResponse address;

    public FeignCompanyResponse(Company company) {
        this.companyId = company.getCompanyId();
        this.hubId = company.getHubId();
        this.companyName = company.getCompanyName();
        this.address = new AddressResponse(company.getAddress().getPostalCode(),
            company.getAddress().getStreetAddress(),
            company.getAddress().getDetailAddress());
    }
}
