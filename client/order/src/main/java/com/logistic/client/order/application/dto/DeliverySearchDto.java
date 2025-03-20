package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySearchDto extends PageRequestDto {
    private UUID supplierDeliveryManagerId;
    private UUID receiverDeliveryManagerId;
}
