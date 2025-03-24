package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryManagerResponse {
    private UUID deliveryManagerId;
    private String username;
}
