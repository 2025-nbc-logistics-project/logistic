package com.logistic.client.company.domain.model.company;

import com.logistic.client.company.presentation.request.CompanyCreateRequestDto;
import com.logistic.client.company.domain.exception.company.CompanyUpdateException;
import com.logistic.client.company.domain.exception.company.CompanyValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    private Company company;

    @BeforeEach
    void setUp() {

        UUID hubId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CompanyType companyType = CompanyType.producer;
        String companyName = "업체1";
        String companyTel = "07012345678";
        String postalCode = "01234";
        String streetAddress = "서울시 중구";
        String detailAddress = "1층";

        company = new Company(new CompanyCreateRequestDto(hubId, companyType, companyName, companyTel, postalCode, streetAddress, detailAddress), userId);
    }

    @Test
    @DisplayName("업체 타입 변경 성공")
    void changeCompanyTypeSuccess() {

        CompanyType newCompanyType = CompanyType.supplier;

        company.changeCompanyType(newCompanyType);

        assertEquals(newCompanyType, company.getCompanyType());
    }

    @Test
    @DisplayName("업체 타입 변경 실패")
    void changeCompanyTypeFailure() {
        assertThrows(IllegalArgumentException.class, () -> company.changeCompanyType(null));
    }

    @Test
    @DisplayName("업체 이름 변경 성공")
    void changeCompanyNameSuccess() {

        String newCompanyName = "업체999";

        company.changeCompanyName(newCompanyName);

        assertEquals(newCompanyName, company.getCompanyName());
    }

    @Test
    @DisplayName("업체 이름 변경 실패")
    void changeCompanyNameFailure() {
        assertThrows(CompanyUpdateException.class, () -> company.changeCompanyName(null));
    }

    @Test
    @DisplayName("업체 전화번호 변경 성공")
    void changeCompanyTelSuccess() {

        String newCompanyTel = "07000000000";

        company.changeCompanyTel(newCompanyTel);

        assertEquals(newCompanyTel, company.getCompanyTel());
    }

    @Test
    @DisplayName("업체 전화번호 변경 실패")
    void changeCompanyTelFailure() {
        assertThrows(CompanyUpdateException.class, () -> company.changeCompanyTel(null));
    }

    @Test
    @DisplayName("업체 주소 변경 성공")
    void changeAddressSuccess() {

        Address address = company.getAddress();

        String newPostalCode = "22222";
        String newStreetAddress = "서울시 종로구";
        String newDetailAddress = "2층";

        company.changeAddress(new Address(newPostalCode, newStreetAddress, newDetailAddress));

        assertEquals(newPostalCode, company.getAddress().getPostalCode());
        assertEquals(newStreetAddress, company.getAddress().getStreetAddress());
        assertEquals(newDetailAddress, company.getAddress().getDetailAddress());
        assertNotEquals(address, company.getAddress());
    }

    @Test
    @DisplayName("업체 주소 변경 실패")
    void changeAddressFailure() {
        assertThrows(CompanyValidationException.class, () -> company.changeAddress(new Address(null, null, null)));
    }

}