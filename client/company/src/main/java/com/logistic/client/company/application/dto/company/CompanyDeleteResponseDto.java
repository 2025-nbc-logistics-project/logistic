package com.logistic.client.company.application.dto.company;

import com.logistic.client.company.domain.model.company.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyDeleteResponseDto {

    private UUID companyId;
    private LocalDateTime deletedAt;
    private long deletedBy;

    public CompanyDeleteResponseDto(Company company) {
        this.companyId = company.getCompanyId();
        this.deletedAt = company.getDeletedAt();
        this.deletedBy = company.getDeletedBy();
    }
}
