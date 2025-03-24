package com.logistic.client.company.presentation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private T data;
    private int status;
    private String message;
}
