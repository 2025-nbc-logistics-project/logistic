package com.logistic.client.order.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderSearchDto extends PageRequestDto {
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private Long userId;
}
