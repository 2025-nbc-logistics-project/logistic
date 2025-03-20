package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchDto extends PageRequestDto {
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private Long userId;
}
