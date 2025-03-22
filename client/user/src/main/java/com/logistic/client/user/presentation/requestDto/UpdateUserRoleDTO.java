package com.logistic.client.user.presentation.requestDto;

import com.logistic.client.user.domain.model.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateUserRoleDTO {
    @NotNull(message = "변경할 역할을 입력해주세요.")
    private UserRole userRole;
}
