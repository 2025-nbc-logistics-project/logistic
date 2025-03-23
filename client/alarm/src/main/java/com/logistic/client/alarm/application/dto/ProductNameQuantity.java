package com.logistic.client.alarm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductNameQuantity {
    private String productName;
    private int quantity;
}
