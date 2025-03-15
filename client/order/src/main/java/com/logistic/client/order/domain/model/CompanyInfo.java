package com.logistic.client.order.domain.model;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class CompanyInfo {
    private final UUID receiverCompanyId;
    private final UUID supplierCompanyId;

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
