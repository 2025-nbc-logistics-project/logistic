package com.logistic.client.user.application.dto.responseDto;

import com.logistic.client.user.domain.model.DeliveryManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDeliveryManagerResDTO {
    private UUID deliveryManagerId;
    private String username;

    public static CompanyDeliveryManagerResDTO to(DeliveryManager manager) {
        return CompanyDeliveryManagerResDTO.builder()
                .deliveryManagerId(manager.getDeliveryManagerId())
                .username(manager.getUser().getUsername())
                .build();
    }
}
