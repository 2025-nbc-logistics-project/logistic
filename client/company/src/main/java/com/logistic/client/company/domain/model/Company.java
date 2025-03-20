package com.logistic.client.company.domain.model;

import com.logistic.client.company.application.dto.company.CompanyCreateRequestDto;
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
    private UUID hudId;

    @Column(name = "user_id", nullable = false)
    private Long userId = 1L; //temp

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
        this.hudId = requestDto.getHudId();
        this.companyType = requestDto.getCompanyType();
        this.companyName = requestDto.getCompanyName();
        this.companyTel = requestDto.getCompanyTel();
        this.address = new Address(requestDto.getPostalCode(), requestDto.getStreetAddress(), requestDto.getDetailAddress());
    }

    public void changeCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public void changeCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void changeCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }

    public void changeAddress(Address address) {
        this.address = address;
    }

}
