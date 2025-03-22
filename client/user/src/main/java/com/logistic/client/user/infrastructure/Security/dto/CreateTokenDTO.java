package com.logistic.client.user.infrastructure.Security.dto;

import com.logistic.client.user.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateTokenDTO {
    private UUID userId;
    private String username;
    private UserRole role;
    private String slackId;
}
