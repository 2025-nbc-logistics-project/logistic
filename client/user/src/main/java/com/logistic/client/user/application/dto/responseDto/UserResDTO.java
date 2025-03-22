package com.logistic.client.user.application.dto.responseDto;

import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResDTO {
    private String username;
    private String slackId;
    private UserRole role;
    private UUID hubId;
    private UUID companyId;

    public static UserResDTO from(User user) {
        return UserResDTO.builder()
                .username(user.getUsername())
                .slackId(user.getSlackId())
                .role(user.getRole())
                .hubId(user.getHubId())
                .companyId(user.getCompanyId())
                .build();
    }
}
