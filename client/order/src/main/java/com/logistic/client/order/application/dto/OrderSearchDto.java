package com.logistic.client.order.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderSearchDto {
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private Long userId;

    private String sortBy = "createdAt";
    private Boolean isAsc = false;

    private int page = 0;
    private int size = 10;

    public void validateSize(int size) {
        if (size != 10 && size != 30 && size != 50) {
            this.size = 10;
        } else {
            this.size = size;
        }
    }
}
