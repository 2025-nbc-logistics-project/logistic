package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class CompanyInfo {
    private final UUID receiverCompanyId; // 공급 업체 Id
    private final UUID supplierCompanyId; // 수령 업체 Id

    protected CompanyInfo() {
        this.receiverCompanyId = null;
        this.supplierCompanyId = null;
    }

    public CompanyInfo(UUID receiverCompanyId, UUID supplierCompanyId) {
        if (receiverCompanyId == null || supplierCompanyId == null || receiverCompanyId.equals(supplierCompanyId)) {
            throw new IllegalArgumentException("업체 Id를 다시 확인해주세요.");
        }
        this.receiverCompanyId = receiverCompanyId;
        this.supplierCompanyId = supplierCompanyId;
    }
}
