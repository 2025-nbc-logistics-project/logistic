package com.logistic.client.user.application.dto.responseDto;

import com.logistic.client.user.domain.model.DeliveryManager;
import com.logistic.client.user.domain.model.DeliveryManagerType;
import com.logistic.client.user.domain.model.User;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryManagerResDTO {
    private String username;
    private UUID hubId;
    private DeliveryManagerType deliveryManagerType;
    private int assignmentOrder;

    public static DeliveryManagerResDTO to(User user, DeliveryManager manager) {
        return DeliveryManagerResDTO.builder()
                .username(user.getUsername())
                .hubId(manager.getHubId())
                .deliveryManagerType(manager.getDeliveryManagerType())
                .assignmentOrder(manager.getAssignmentOrder())
                .build();
    }

    public static DeliveryManagerResDTO to(DeliveryManager manager) {
        return DeliveryManagerResDTO.builder()
                .username(manager.getUser().getUsername())
                .hubId(manager.getHubId())
                .deliveryManagerType(manager.getDeliveryManagerType())
                .assignmentOrder(manager.getAssignmentOrder())
                .build();
    }
}
