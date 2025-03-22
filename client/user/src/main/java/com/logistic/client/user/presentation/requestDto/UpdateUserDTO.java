package com.logistic.client.user.presentation.requestDto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateUserDTO {
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 최소 8~15자이며 대소문자, 숫자, 특수문자가 포함되어야 합니다.")
    private String password;

    private String slackId;
}
