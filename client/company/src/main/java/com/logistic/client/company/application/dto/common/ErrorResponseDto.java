package com.logistic.client.company.application.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {

    private int status;
    private String message;
    private List<String> errors;

    public ErrorResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
