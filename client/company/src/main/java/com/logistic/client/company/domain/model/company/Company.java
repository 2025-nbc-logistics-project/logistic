package com.logistic.client.company.domain.model.company;

import com.logistic.client.company.domain.exception.common.HubNotFoundException;
import com.logistic.client.company.presentation.request.CompanyCreateRequestDto;
import com.logistic.client.company.domain.exception.company.CompanyUpdateException;
import com.logistic.client.company.domain.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_company")
public class Company extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "company_id", nullable = false, updatable = false, unique = true)
    private UUID companyId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "user_id", nullable = false)
    private UUID userId; //업체 관리자

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_tel", nullable = false)
    private String companyTel;

    @Embedded
    private Address address;

    public Company(CompanyCreateRequestDto requestDto){
        this.hubId = requestDto.getHubId();
        this.userId = requestDto.getUserId();
        this.companyType = requestDto.getCompanyType();
        this.companyName = requestDto.getCompanyName();
        this.companyTel = requestDto.getCompanyTel();
        this.address = new Address(requestDto.getPostalCode(), requestDto.getStreetAddress(), requestDto.getDetailAddress());
    }

    public void changeHub(UUID hubId) {
        if(hubId == null) {
            throw new HubNotFoundException();
        }
        this.hubId = hubId;
    }

    public void changeCompanyType(CompanyType companyType) {
        if(companyType == null) {
            throw new IllegalArgumentException();
        }
        this.companyType = companyType;
    }

    public void changeCompanyName(String companyName) {
        if(companyName == null || companyName.isBlank()) {
            throw new CompanyUpdateException();
        }
        this.companyName = companyName;
    }

    public void changeCompanyTel(String companyTel) {
        if(companyTel == null || companyTel.isBlank()) {
            throw new CompanyUpdateException();
        }
        this.companyTel = companyTel;
    }

    public void changeAddress(Address address) {
        this.address = new Address(address.getPostalCode(), address.getStreetAddress(), address.getDetailAddress());
    }

}
