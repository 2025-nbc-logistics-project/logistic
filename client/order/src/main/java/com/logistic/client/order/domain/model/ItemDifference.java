package com.logistic.client.order.domain.model;

import com.logistic.client.order.application.dto.OrderItemRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDifference {
    private List<OrderItemRequestDto> restoreList;
    private List<OrderItemRequestDto> deductList;
}
