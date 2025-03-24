package com.logistic.client.company.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequestDto {

    private UUID hubId;
    private String productName;
    private Integer price;
    private Integer quantity;

}
