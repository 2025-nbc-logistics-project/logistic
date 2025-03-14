package com.logistic.client.order.domain.model;

import java.util.UUID;

public class CompanyInfo {
    private final UUID receiverCompanyId;
    private final UUID supplierCompanyId;

    public CompanyInfo(UUID receiverCompanyId, UUID supplierCompanyId) {
        if (receiverCompanyId == null || supplierCompanyId == null || receiverCompanyId == supplierCompanyId) {
            throw new IllegalArgumentException("업체 Id를 다시 확인해주세요.");
        }
        this.receiverCompanyId = receiverCompanyId;
        this.supplierCompanyId = supplierCompanyId;
    }
}
