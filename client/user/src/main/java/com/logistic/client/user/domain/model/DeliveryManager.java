package com.logistic.client.user.domain.model;

import com.logistic.client.user.application.dto.responseDto.DeliveryManagerResDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "p_deliveryManager")
public class DeliveryManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryManagerId;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private UUID hubId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryManagerType deliveryManagerType;

    private int assignmentOrder;
}
