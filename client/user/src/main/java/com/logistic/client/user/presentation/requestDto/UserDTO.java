package com.logistic.client.user.presentation.requestDto;

import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "회원가입 할 이름을 입력해주세요.")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "유저명은 소문자(a~z), 숫자(0~9)로 4~10자여야 합니다.")
    private String username;

    @NotBlank(message = "회원가입 할 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 최소 8~15자이며 대소문자, 숫자, 특수문자가 포함되어야 합니다.")
    private String password;

    @NotBlank(message = "슬랙 아이디를 입력해주세요.")
    private String slackId;

    @NotNull(message = "유저의 권한을 입력해주세요.")
    private UserRole role;

    //허브 관리자용
    private UUID hubId;

    //업체 관리자용
    private UUID companyId;

    //기본값 일반 유저로 회원가입
    public User toUser(String encodePassword) {
        return User.builder()
                .username(username)
                .password(encodePassword)
                .slackId(slackId)
                .role(role)
                .hubId(hubId)
                .companyId(companyId)
                .build();
    }
}
