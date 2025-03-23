package com.logistic.client.company.application.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class HubDto {
    private UUID id;
    private String name;
}

