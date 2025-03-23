package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerResponse {
    private UUID deliveryManagerId;
    private String deliveryManagerName;
}
