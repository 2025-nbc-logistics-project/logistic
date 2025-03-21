package com.logistic.client.user.domain.model;


import com.logistic.client.user.application.dto.responseDto.UserResDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "p_user")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String slackId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private UUID hubId;

    private UUID companyId;

    public UserResDTO toResponseDto() {
        return UserResDTO.builder()
                .username(username)
                .password(password)
                .slackId(slackId)
                .role(role)
                .hubId(hubId)
                .companyId(companyId)
                .build();
    }
}
