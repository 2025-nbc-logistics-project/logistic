package com.logistic.client.user.infrastructure.Security.dto;

import com.logistic.client.user.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTokenDTO {
    private String username;
    private UserRole role;
}
