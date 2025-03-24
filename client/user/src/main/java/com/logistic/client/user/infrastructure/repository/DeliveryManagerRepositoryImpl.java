package com.logistic.client.user.infrastructure.repository;

import com.logistic.client.user.domain.model.DeliveryManager;
import com.logistic.client.user.domain.model.DeliveryManagerType;
import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.repository.DeliveryManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {
    private final JpaDeliveryManagerRepository deliveryManagerRepository;

    @Override
    public Boolean existsByUserAndIsDeletedFalse(User user) {
        return deliveryManagerRepository.existsByUserAndIsDeletedFalse(user);
    }

    @Override
    public List<DeliveryManager> findAllByHubIdAndDeliveryManagerTypeAndIsDeletedFalse(UUID hubId, DeliveryManagerType type) {
        return deliveryManagerRepository.findAllByHubIdAndDeliveryManagerTypeAndIsDeletedFalse(hubId, type);
    }

    @Override
    public List<DeliveryManager> findAllByDeliveryManagerTypeAndIsDeletedFalse(DeliveryManagerType type) {
        return deliveryManagerRepository.findAllByDeliveryManagerTypeAndIsDeletedFalse(type);
    }

    @Override
    public DeliveryManager save(DeliveryManager deliveryManager) {
        return deliveryManagerRepository.save(deliveryManager);
    }

    @Override
    public Optional<DeliveryManager> findByDeliveryManagerIdAndIsDeletedFalse(UUID deliveryManagerId) {
        return deliveryManagerRepository.findByDeliveryManagerIdAndIsDeletedFalse(deliveryManagerId);
    }

    @Override
    public List<DeliveryManager> findAllByHubIdAndIsDeletedFalseOrderByAssignmentOrderDesc(UUID hubId) {
        return deliveryManagerRepository.findAllByHubIdAndIsDeletedFalseOrderByAssignmentOrderDesc(hubId);
    }

    @Override
    public List<DeliveryManager> findAllByDeliveryManagerTypeAndIsDeletedFalseOrderByAssignmentOrderDesc(DeliveryManagerType deliveryManagerType) {
        return deliveryManagerRepository.findAllByDeliveryManagerTypeAndIsDeletedFalseOrderByAssignmentOrderDesc(deliveryManagerType);
    }
}
