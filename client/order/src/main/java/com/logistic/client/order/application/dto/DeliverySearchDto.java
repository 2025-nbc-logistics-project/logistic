package com.logistic.client.order.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliverySearchDto extends PageRequestDto {
    private UUID supplierDeliveryManagerId;
    private UUID receiverDeliveryManagerId;
}
