package com.logistic.client.company.domain.model.company;

import com.logistic.client.company.domain.exception.company.CompanyValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "detail_addresss")
    private String detailAddress;

    public Address(String postalCode, String streetAddress, String detailAddress) {
        if (postalCode == null || streetAddress == null) {
            throw new CompanyValidationException();
        }
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress == null ? "" : detailAddress;
    }

}
