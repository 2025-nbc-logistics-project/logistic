package com.logistic.client.company.presentation.request;

import com.logistic.client.company.domain.model.company.CompanyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "업체 수정 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateRequestDto {

    @Schema(description = "소속 허브 UUID", example = "e2eea1ab-463e-42e7-82d5-0da14235fe5a")
    private UUID hubId;
    @Schema(description = "업체 타입(producer, supplier)", example = "supplier")
    private CompanyType companyType;
    @Schema(description = "업체 이름", example = "생성 업체999")
    private String companyName;
    @Schema(description = "업체 전화번호", example = "07099999999")
    private String companyTel;
    @Schema(description = "업체 우편번호", example = "99999")
    private String postalCode;
    @Schema(description = "업체 주소", example = "서울시 종로구")
    private String streetAddress;
    @Schema(description = "업체 상세 주소", example = "1층")
    private String detailAddress;

}
