package com.logistic.client.user.application.dto.requestDto;

import com.logistic.client.user.domain.model.DeliveryManager;
import com.logistic.client.user.domain.model.DeliveryManagerType;
import com.logistic.client.user.domain.model.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryManagerDTO {
    @NotBlank(message = "유저 이름을 입력해주세요.")
    private String username;

    private UUID hubId;

    @NotBlank(message = "배송 담당 타입을 입력해주세요.")
    private DeliveryManagerType deliveryManagerType;

    @AssertTrue(message = "COMPANY_DELIVERY_MANAGER 타입일 경우 허브 아이디는 필수 입력값입니다.")
    public boolean isHubIdValid() {
        return deliveryManagerType != DeliveryManagerType.COMPANY_DELIVERY_MANAGER || hubId != null;
    }

    public DeliveryManager toDeliveryManager(User user, int assignmentOrder) {
        return DeliveryManager.builder()
                .user(user)
                .hubId(hubId)
                .deliveryManagerType(deliveryManagerType)
                .assignmentOrder(assignmentOrder)
                .build();
    }
}
