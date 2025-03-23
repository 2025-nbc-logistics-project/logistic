package com.logistic.client.delivery.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySearchDto extends PageRequestDto {
    private UUID supplierDeliveryManagerId;
    private UUID receiverDeliveryManagerId;
    @Setter
    private UUID userId;
}
