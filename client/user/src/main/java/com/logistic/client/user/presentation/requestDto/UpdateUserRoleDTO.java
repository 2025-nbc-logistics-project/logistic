package com.logistic.client.user.presentation.requestDto;

import com.logistic.client.user.domain.model.DeliveryManagerType;
import com.logistic.client.user.domain.model.UserRole;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateUserRoleDTO {
    @NotNull(message = "변경할 역할을 입력해주세요.")
    private UserRole userRole;

    //허브 관리자용
    private UUID hubId;

    //업체 관리자용
    private UUID companyId;

    @AssertTrue(message = "HUB_MANAGER 타입일 경우 허브 아이디는 필수 입력값입니다.")
    public boolean isHubIdValid() {
        return userRole != UserRole.HUB_MANAGER || hubId != null;
    }

    @AssertTrue(message = "COMPANY_MANAGER 타입일 경우 업체 아이디는 필수 입력값입니다.")
    public boolean isCompanyValid() {
        return userRole != UserRole.COMPANY_MANAGER || companyId != null;
    }
}
