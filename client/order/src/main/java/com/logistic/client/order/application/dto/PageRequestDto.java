package com.logistic.client.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class PageRequestDto {
    private String sortBy = "createdAt";
    private Boolean isAsc = false;

    private int page = 0;
    private int size = 10;

    public void validateSize(int size) {
        if (size != 10 && size != 30 && size != 50) {
            this.size = 10;
        } else {
            this.size = size;
        }
    }
}