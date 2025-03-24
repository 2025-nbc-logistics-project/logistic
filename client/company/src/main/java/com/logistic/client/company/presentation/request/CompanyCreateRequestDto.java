package com.logistic.client.company.presentation.request;

import com.logistic.client.company.domain.model.company.CompanyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Schema(description = "업체 등록 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequestDto {

    @Schema(description = "소속 허브 UUID", example = "e2eea1ab-463e-42e7-82d5-0da14235fe5a")
    @NotNull(message = "소속 허브를 입력해주세요.")
    private UUID hubId;

    @Schema(description = "업체 타입(producer, supplier)", example = "producer")
    @NotNull(message = "업체 타입을 입력해주세요.")
    private CompanyType companyType;

    @Schema(description = "업체 이름", example = "공급 업체1")
    @NotBlank(message = "업체 이름을 입력해주세요.")
    private String companyName;

    @Schema(description = "업체 전화번호", example = "07012345678")
    @NotBlank(message = "업체 전화번호를 입력해주세요.")
    @Size(min = 9, max = 11, message = "업체 전화번호는 9자리 이상 11자리 이하여야 합니다.")
    private String companyTel;

    @Schema(description = "업체 우편번호", example = "01234")
    @NotBlank(message = "업체 우편번호를 입력해주세요.")
    private String postalCode;

    @Schema(description = "업체 주소", example = "서울시 중구")
    @NotBlank(message = "업체 주소를 입력해주세요.")
    private String streetAddress;

    @Schema(description = "업체 상세 주소", example = "1층")
    private String detailAddress;

    public CompanyCreateRequestDto(CompanyCreateRequestDto requestDto) {
        this.hubId = requestDto.getHubId();
        this.companyType = requestDto.getCompanyType();
        this.companyName = requestDto.getCompanyName();
        this.companyTel = requestDto.getCompanyTel();
        this.postalCode = requestDto.getPostalCode();
        this.streetAddress = requestDto.getStreetAddress();
        this.detailAddress = requestDto.getDetailAddress();
    }

}
