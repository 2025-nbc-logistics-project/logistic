package com.logistic.client.company.application.dto.company;

import com.logistic.client.company.domain.model.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequestDto {

    @NotNull(message = "소속 허브를 입력해주세요.")
    private UUID hudId;

    @NotNull(message = "업체 타입을 입력해주세요.")
    private CompanyType companyType;

    @NotBlank(message = "업체 이름을 입력해주세요.")
    private String companyName;

    @NotBlank(message = "업체 전화번호를 입력해주세요.")
    @Size(min = 9, max = 11, message = "업체 전화번호는 9자리 이상 11자리 이하여야 합니다.")
    private String companyTel;

    @NotBlank(message = "업체 우편 번호를 입력해주세요.")
    private String postalCode;

    @NotBlank(message = "업체 주소를 입력해주세요.")
    private String streetAddress;

    private String detailAddress;

}
