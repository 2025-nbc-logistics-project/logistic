package com.logistic.client.user.domain.repository;

import com.logistic.client.user.domain.model.DeliveryManager;
import com.logistic.client.user.domain.model.DeliveryManagerType;
import com.logistic.client.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository {
    Boolean existsByUserAndIsDeletedFalse(User user);
    List<DeliveryManager> findAllByHubIdAndDeliveryManagerTypeAndIsDeletedFalse(UUID hubId, DeliveryManagerType type);
    List<DeliveryManager> findAllByDeliveryManagerTypeAndIsDeletedFalse(DeliveryManagerType type);
    DeliveryManager save(DeliveryManager deliveryManager);
    Optional<DeliveryManager> findByDeliveryManagerIdAndIsDeletedFalse(UUID deliveryManagerId);
    List<DeliveryManager> findAllByHubIdAndIsDeletedFalseOrderByAssignmentOrderDesc(UUID hubId);
    List<DeliveryManager> findAllByDeliveryManagerTypeAndIsDeletedFalseOrderByAssignmentOrderDesc(DeliveryManagerType deliveryManagerType);
}
