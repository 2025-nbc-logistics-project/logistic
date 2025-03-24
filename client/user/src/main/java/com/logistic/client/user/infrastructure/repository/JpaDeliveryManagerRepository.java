package com.logistic.client.user.infrastructure.repository;

import com.logistic.client.user.domain.model.DeliveryManager;
import com.logistic.client.user.domain.model.DeliveryManagerType;
import com.logistic.client.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaDeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {
    Boolean existsByUserAndIsDeletedFalse(User user);
    List<DeliveryManager> findAllByHubIdAndDeliveryManagerTypeAndIsDeletedFalse(UUID hubId, DeliveryManagerType type);
    List<DeliveryManager> findAllByDeliveryManagerTypeAndIsDeletedFalse(DeliveryManagerType type);
    DeliveryManager save(DeliveryManager deliveryManager);
    Optional<DeliveryManager> findByDeliveryManagerIdAndIsDeletedFalse(UUID deliveryManagerId);
    List<DeliveryManager> findAllByHubIdAndIsDeletedFalseOrderByAssignmentOrderDesc(UUID hubId);
    List<DeliveryManager> findAllByDeliveryManagerTypeAndIsDeletedFalseOrderByAssignmentOrderDesc(DeliveryManagerType deliveryManagerType);

}
