package com.logistic.client.company.application.dto.company;

import com.logistic.client.company.domain.model.CompanyType;
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

    private UUID hudId;
    private CompanyType companyType;
    private String companyName;
    private String companyTel;
    private String postalCode;
    private String streetAddress;
    private String detailAddress;
    //담당자
    //private Long userId;

}
