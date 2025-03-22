package com.logistic.client.user.application.service;

import com.logistic.client.user.presentation.requestDto.DeliveryManagerDTO;
import com.logistic.client.user.presentation.requestDto.UpdateDeliveryManagerDTO;
import com.logistic.client.user.application.dto.responseDto.DeliveryManagerResDTO;
import com.logistic.client.user.domain.model.DeliveryManager;
import com.logistic.client.user.domain.model.DeliveryManagerType;
import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import com.logistic.client.user.infrastructure.configuration.customException.AccessDeniedException;
import com.logistic.client.user.infrastructure.configuration.customException.CompanyDeliveryManagerCountMaxException;
import com.logistic.client.user.infrastructure.configuration.customException.HubDeliveryManagerCountMaxException;
import com.logistic.client.user.infrastructure.repository.DeliveryManagerRepositoryImpl;
import com.logistic.client.user.infrastructure.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {
    private final UserRepositoryImpl userRepository;
    private final DeliveryManagerRepositoryImpl deliveryManagerRepository;

    public DeliveryManagerResDTO createDeliveryManager(DeliveryManagerDTO requestDto, String userRole, String signInUsername) {
        try {
            UserRole role = UserRole.valueOf(userRole);
            User signInUser = userRepository.findByUsernameAndIsDeletedFalse(signInUsername)
                    .orElseThrow(() -> new IllegalArgumentException("현재 로그인한 사용자가 존재하지 않는 유저입니다."));

            if(role.equals(UserRole.MASTER) || role.equals(UserRole.HUB_MANAGER)) {
                //허브 관리자일경우 담당 허브의 배송 담당자만 관리할 수 있도록.
                if(role.equals(UserRole.HUB_MANAGER) && signInUser.getHubId() != requestDto.getHubId()) {
                    throw new AccessDeniedException("담당 허브가 아니므로 배송 담당자를 등록할 수 없습니다.");
                }

                int assignmentOrder = 1;
                if(requestDto.getDeliveryManagerType().equals(DeliveryManagerType.COMPANY_DELIVERY_MANAGER)) {
                    //허브 아이디로 허브 존재 유무 체크 필요

                    List<DeliveryManager> existCompanyManagers = deliveryManagerRepository.findAllByHubIdAndDeliveryManagerTypeAndIsDeletedFalse(requestDto.getHubId(), requestDto.getDeliveryManagerType());
                    if(existCompanyManagers.size() == 10)
                        throw new CompanyDeliveryManagerCountMaxException("업체 배송 담당자가 최대 인원입니다.");

                    assignmentOrder = getCompanyAssignmentOrder(requestDto.getHubId());
                }

                if(requestDto.getDeliveryManagerType().equals(DeliveryManagerType.HUB_DELIVERY_MANAGER)) {
                    List<DeliveryManager> existHubManagers = deliveryManagerRepository.findAllByDeliveryManagerTypeAndIsDeletedFalse(requestDto.getDeliveryManagerType());
                    if(existHubManagers.size() == 10)
                        throw new HubDeliveryManagerCountMaxException("업체 배송 담당자가 최대 인원입니다.");

                    assignmentOrder = getHubAssignmentOrder();
                }
                User user = userRepository.findByUsernameAndIsDeletedFalse(requestDto.getUsername())
                        .orElseThrow(() -> new IllegalArgumentException("배송 담당자로 등록하려는 유저가 존재하지 않는 유저입니다."));
                DeliveryManager manager = requestDto.toDeliveryManager(user, assignmentOrder);
                manager.setCreatedAt(LocalDateTime.now());
                manager.setCreatedBy(signInUsername);

                deliveryManagerRepository.save(manager);
                return DeliveryManagerResDTO.to(user, manager);
            }
            else {
                throw new AccessDeniedException("배송 관리자 정보에 대해 생성 권한이 존재하지 않습니다.");
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    public DeliveryManagerResDTO getDeliveryManager(String deliveryManagerId, String userRole, String signInUsername) {
        try {
            UserRole role = UserRole.valueOf(userRole);
            User user = userRepository.findByUsernameAndIsDeletedFalse(signInUsername)
                    .orElseThrow(() -> new IllegalArgumentException("현재 로그인한 사용자가 존재하지 않는 유저입니다."));

            DeliveryManager deliveryManager = deliveryManagerRepository.findByDeliveryManagerIdAndIsDeletedFalse(UUID.fromString(deliveryManagerId))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송 담당자입니다."));

            if(role.equals(UserRole.COMPANY_MANAGER))
                throw new AccessDeniedException("배송 담당자를 조회 할 수 있는 권한이 없습니다.");

            if(role.equals(UserRole.DELIVERY_MANAGER) && deliveryManager.getUser().getUserId() != user.getUserId())
                throw new AccessDeniedException("본인의 정보만 조회 가능합니다.");

            if(role.equals(UserRole.HUB_MANAGER) && deliveryManager.getHubId() != user.getHubId())
                throw new AccessDeniedException("담당 허브의 배송 담당자만 조회 가능합니다.");

            return DeliveryManagerResDTO.to(user, deliveryManager);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public DeliveryManagerResDTO updateDeliveryManagers(String deliveryManagerId, UpdateDeliveryManagerDTO requestDto, String userRole, String signInUsername) {
        try {
            UserRole role = UserRole.valueOf(userRole);
            User signInUser = userRepository.findByUsernameAndIsDeletedFalse(signInUsername)
                    .orElseThrow(() -> new IllegalArgumentException("현재 로그인한 사용자가 존재하지 않는 유저입니다."));

            DeliveryManager deliveryManager = deliveryManagerRepository.findByDeliveryManagerIdAndIsDeletedFalse(UUID.fromString(deliveryManagerId))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송 담당자입니다."));

            if(role.equals(UserRole.MASTER) || role.equals(UserRole.HUB_MANAGER)) {
                //허브 관리자일경우 담당 허브의 배송 담당자만 관리할 수 있도록.
                if(role.equals(UserRole.HUB_MANAGER) && signInUser.getHubId() != requestDto.getHubId()) {
                    throw new AccessDeniedException("담당 허브가 아니므로 배송 담당자를 수정할 수 없습니다.");
                }

                if(requestDto.getDeliveryManagerType().equals(DeliveryManagerType.COMPANY_DELIVERY_MANAGER)) {
                    //허브 아이디로 허브 존재 유무 체크 필요

                    List<DeliveryManager> existCompanyManagers = deliveryManagerRepository.findAllByHubIdAndDeliveryManagerTypeAndIsDeletedFalse(requestDto.getHubId(), requestDto.getDeliveryManagerType());
                    if(existCompanyManagers.size() == 10)
                        throw new CompanyDeliveryManagerCountMaxException("업체 배송 담당자가 최대 인원입니다.");
                }

                if(requestDto.getDeliveryManagerType().equals(DeliveryManagerType.HUB_DELIVERY_MANAGER)) {
                    List<DeliveryManager> existHubManagers = deliveryManagerRepository.findAllByDeliveryManagerTypeAndIsDeletedFalse(requestDto.getDeliveryManagerType());
                    if(existHubManagers.size() == 10)
                        throw new HubDeliveryManagerCountMaxException("업체 배송 담당자가 최대 인원입니다.");
                }

                deliveryManager.setUpdatedAt(LocalDateTime.now());
                deliveryManager.setUpdatedBy(signInUsername);
                deliveryManager.setDeliveryManagerType(requestDto.getDeliveryManagerType());
                if(requestDto.getHubId() != null)
                    deliveryManager.setHubId(requestDto.getHubId());

                deliveryManagerRepository.save(deliveryManager);
                return DeliveryManagerResDTO.to(deliveryManager);
            }
            else {
                throw new AccessDeniedException("배송 관리자 정보에 대해 수정 권한이 존재하지 않습니다.");
            }
        }
        catch(Exception e) {
            throw e;
        }
    }

    public void deleteDeliveryManager(String deliveryManagerId, String userRole, String signInUsername) {
        try {
            UserRole role = UserRole.valueOf(userRole);
            User signInUser = userRepository.findByUsernameAndIsDeletedFalse(signInUsername)
                    .orElseThrow(() -> new IllegalArgumentException("현재 로그인한 사용자가 존재하지 않는 유저입니다."));

            DeliveryManager deliveryManager = deliveryManagerRepository.findByDeliveryManagerIdAndIsDeletedFalse(UUID.fromString(deliveryManagerId))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송 담당자입니다."));

            if(role.equals(UserRole.MASTER) || role.equals(UserRole.HUB_MANAGER)) {
                //허브 관리자일경우 담당 허브의 배송 담당자만 관리할 수 있도록.
                if(role.equals(UserRole.HUB_MANAGER) && signInUser.getHubId() != deliveryManager.getHubId()) {
                    throw new AccessDeniedException("담당 허브가 아니므로 배송 담당자를 삭제할 수 없습니다.");
                }

                deliveryManager.setDeletedAt(LocalDateTime.now());
                deliveryManager.setDeletedBy(signInUsername);
                deliveryManagerRepository.save(deliveryManager);
            }
            else {
                throw new AccessDeniedException("배송 관리자 정보에 대해 삭제 권한이 존재하지 않습니다.");
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    private int getHubAssignmentOrder() {
        int assignmentOrder = 1;
        List<DeliveryManager> deliveryManagers = deliveryManagerRepository.findAllByDeliveryManagerTypeAndIsDeletedFalseOrderByAssignmentOrderDesc(DeliveryManagerType.HUB_DELIVERY_MANAGER);

        if(deliveryManagers != null && !deliveryManagers.isEmpty()) {
            return assignmentOrder + deliveryManagers.get(0).getAssignmentOrder();
        }
        return assignmentOrder;
    }

    private int getCompanyAssignmentOrder(UUID hubId) {
        int assignmentOrder = 1;
        List<DeliveryManager> deliveryManagers = deliveryManagerRepository.findAllByHubIdAndIsDeletedFalseOrderByAssignmentOrderDesc(hubId);

        if(deliveryManagers != null && !deliveryManagers.isEmpty()) {
            return assignmentOrder + deliveryManagers.get(0).getAssignmentOrder();
        }
        return assignmentOrder;
    }
}
