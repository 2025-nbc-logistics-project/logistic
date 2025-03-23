package com.logistic.client.company.presentation.request;

import com.logistic.client.company.domain.model.company.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateRequestDto {

    private UUID hubId;
    private CompanyType companyType;
    private String companyName;
    private String companyTel;
    private String postalCode;
    private String streetAddress;
    private String detailAddress;

}
