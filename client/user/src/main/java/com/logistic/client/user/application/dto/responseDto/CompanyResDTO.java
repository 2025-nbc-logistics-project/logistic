package com.logistic.client.user.application.dto.responseDto;

import java.util.UUID;

public class CompanyResDTO {
    private UUID companyId;
    private UUID hudId;
    private Long userId;
    private CompanyType companyType;
    private String companyName;
    private String companyTel;
    private String postalCode;
    private String streetAddress;
    private String detailAddress;
}

enum CompanyType {
    producer, //생산 업체
    supplier //공급 업체
}
