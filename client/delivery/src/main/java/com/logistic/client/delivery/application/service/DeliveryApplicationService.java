package com.logistic.client.delivery.application.service;

import com.logistic.client.delivery.application.dto.*;
import com.logistic.client.delivery.domain.exception.DeliveryNotFoundException;
import com.logistic.client.delivery.domain.exception.UnauthorizedException;
import com.logistic.client.delivery.domain.model.*;
import com.logistic.client.delivery.domain.repository.DeliveryRepository;
import com.logistic.client.delivery.domain.service.DeliveryDomainService;
import com.logistic.client.delivery.infrastructure.client.HubClient;
import com.logistic.client.delivery.infrastructure.client.OrderClient;
import com.logistic.client.delivery.presentation.request.DeliverySearchDto;
import com.logistic.client.delivery.presentation.request.DeliveryUpdateRequestDto;
import com.logistic.client.delivery.presentation.request.routeUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryDomainService deliveryDomainService;
    private final HubClient hubClient;
    private final OrderClient orderClient;

    public FeignDeliveryResponse createDelivery(CreateDeliveryRequest request) {
        // (1). request 를 기반으로 delivery 엔티티 생성
        Delivery delivery = new Delivery(
            request.getOrderId(),
            new DeliveryManagerId(request.getReceiverDeliveryManager(), request.getSupplierDeliveryManager()),
            new DeliveryHubInfo(request.getDepartureHubId(), request.getDestinationHubId()),
            deliveryDomainService.buildShippingInfo(request.getReceiverAddress(), request.getSupplierAddress())
        );

        // (2). Hub 서비스를 호출하여 출발 허브부터 최종 목적지 허브까지의 경로 계산 요청, 각 허브 간의 이동 경로를 List 로 반환 받음
        FindRouteResponse findRouteResponses = hubClient.getHubRoutes(
            request.getDepartureHubId(),
            request.getDestinationHubId()
        );

        List<HubRouteResponse> routeResponses =
            HubRouteMapper.toHubRouteResponses(findRouteResponses, null);

        // (3). 허브 간의 이동 경로 List 를 기반으로 DeliveryRoute 생성 및 delivery 엔티티에 추가
        deliveryDomainService.addRoutesToDelivery(delivery, routeResponses);

        // (4). DB에 저장
        deliveryRepository.save(delivery);

        // TODO : 이벤트 발행 또는 Order 서비스를 호출하여 배송 데이터 업데이트 및 슬랙 메시지 발송 요청 (추후 반영 예정)
        return new FeignDeliveryResponse(delivery);
    }

    @Transactional(readOnly = true)
    public DeliveryResponseDto readDelivery(UUID deliveryId, UUID userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);

        if (!canReadDelivery(delivery, userId, role)) {
            throw new UnauthorizedException("조회 권한이 없습니다.");
        }

        return new DeliveryResponseDto(delivery);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<DeliverySummaryDto> searchDeliveries(DeliverySearchDto searchDto, UUID userId, String role) {
        if (role.equals("DELIVERY_MANAGER")) {
            searchDto.setUserId(userId);
        }
        Page<DeliverySummaryDto> mappedPage = deliveryRepository.searchDeliveries(searchDto).map(DeliverySummaryDto::new);
        return new PageResponseDto<>(mappedPage);
    }

    @Transactional
    public DeliverySummaryDto updateDelivery(UUID deliveryId, DeliveryUpdateRequestDto requestDto, UUID userId, String role) {
        // (1). 배송 조회
        Delivery delivery = findDeliveryById(deliveryId);

        // (2). 권한 검증
        if (!canUpdateDelivery(delivery, userId, role)) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }

        // (3). 변경할 수령 업체 배송 담당자, 공급 업체 배송 담당자가 입력되었다면 Update
        if (requestDto.getReceiverDeliveryManagerId() != null) {
            delivery.updateReceiverManager(requestDto.getReceiverDeliveryManagerId());
        }
        if (requestDto.getSupplierDeliveryManagerId() != null) {
            delivery.updateSupplierManager(requestDto.getSupplierDeliveryManagerId());
        }

        // (4). 수정할 ShippingInfo 데이터가 입력되었다면 반영, 입력되지 않았다면 기존 데이터 사용
        ShippingInfo oldShipping = delivery.getShippingInfo();
        Address newReceiverAddress = deliveryDomainService.buildNewAddress(
            oldShipping.getReceiverAddress(),
            requestDto.getReceiverPostalCode(),
            requestDto.getReceiverDetailAddress(),
            requestDto.getReceiverStreetAddress()
        );

        String newRecipientName
            = requestDto.getRecipientName() != null ? requestDto.getRecipientName() : oldShipping.getRecipientName();
        UUID newRecipientSlackId
            = requestDto.getRecipientSlackId() != null ? requestDto.getRecipientSlackId() : oldShipping.getRecipientSlackId();

        // (5). 새 ShippingInfo 로 Update
        delivery.updateShippingInfo(new ShippingInfo(
            newReceiverAddress,
            oldShipping.getSupplierAddress(),
            newRecipientName,
            newRecipientSlackId
        ));

        return new DeliverySummaryDto(delivery);
    }

    @Transactional
    public DeliverySummaryDto updateDeliveryStatus(UUID deliveryId, DeliveryStatus newStatus, UUID userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);

        if (!canUpdateDelivery(delivery, userId, role)) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }

        delivery.updateStatus(newStatus);

        String newOrderStatusString = mapDeliveryStatusToOrderStatusString(delivery.getStatus());
        if (newOrderStatusString != null) {
            orderClient.updateOrderStatus(delivery.getOrderId(), newOrderStatusString);
        }

        return new DeliverySummaryDto(delivery);
    }

    @Transactional
    public DeliveryRouteDto updateNextRouteStatus(UUID deliveryId, DeliveryRouteStatus newStatus, UUID userId, String role) {
        // (1). 배송 조회
        Delivery delivery = findDeliveryById(deliveryId);

        // (2). 권한 검증
        if (!canUpdateDelivery(delivery, userId, role)) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }

        // (2). 도메인 서비스를 호출하여 타겟 경로(업데이트가 되어야 하는 경로)를 찾음
        DeliveryRoute targetRoute = deliveryDomainService.findNextRouteToUpdate(delivery);

        // (3). 상태 업데이트
        targetRoute.updateRouteStatus(newStatus);

        return new DeliveryRouteDto(targetRoute);
    }

    public DeliveryRouteDto updateActualDistanceTime(UUID deliveryId, UUID routeId, routeUpdateRequestDto requestDto, UUID userId, String role) {
        // (1). Delivery 조회
        Delivery delivery = findDeliveryById(deliveryId);

        // (2). 권한 검증
        if (!canUpdateDelivery(delivery, userId, role)) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }

        // (3). routeId에 해당하는 DeliveryRoute 찾기
        DeliveryRoute targetRoute = delivery.getDeliveryRoutes().stream()
            .filter(r -> r.getDeliveryRouteId().equals(routeId))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 Route가 존재하지 않습니다."));

        // (4). 도메인 서비스를 호출하여 실제 거리/시간 업데이트
        deliveryDomainService.updateActualDistance(targetRoute, requestDto.getDistance(), requestDto.getTime());

        return new DeliveryRouteDto(targetRoute);
    }

    @Transactional
    public void deleteDelivery(UUID deliveryId, UUID userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);

        if (!canDeleteDelivery(delivery, userId, role)) {
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        delivery.markAsDeleted(userId);
    }

    private String mapDeliveryStatusToOrderStatusString(DeliveryStatus status) {
        return switch (status) {
            case WAITING_AT_HUB -> "DELIVERING";
            case DELIVERY_COMPLETED -> "COMPLETED";
            default -> null; // 업데이트 불필요
        };
    }

    private Delivery findDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new DeliveryNotFoundException("해당 Id를 가진 Delivery 가 존재하지 않습니다."));
    }

    private boolean canReadDelivery(Delivery delivery, UUID userId, String role) {
        if (role.equals("MASTER")) return true;

        if (role.equals("HUB_MANAGER")) { // 로직 추가 필요
            return true;
        }

        if (role.equals("DELIVERY_MANAGER")) {
            return isDeliveryManagerOf(delivery, userId);
        }

        return role.equals("COMPANY_MANAGER");
    }

    private boolean canUpdateDelivery(Delivery delivery, UUID userId, String role) {
        if (role.equals("MASTER")) return true;

        if (role.equals("HUB_MANAGER")) {
            return true;
        }

        if (role.equals("DELIVERY_MANAGER")) {
            return isDeliveryManagerOf(delivery, userId);
        }

        return false;
    }

    private boolean canDeleteDelivery(Delivery delivery, UUID userId, String role) {
        if (role.equals("MASTER")) return true;

        return role.equals("HUB_MANAGER");
    }

    private boolean isDeliveryManagerOf(Delivery delivery, UUID userId) {
        // 업체 배송 담당자
        if (delivery.getDeliveryManagerId() != null) {
            if (delivery.getDeliveryManagerId().getReceiverDeliveryManagerId().equals(userId)
                || delivery.getDeliveryManagerId().getSupplierDeliveryManagerId().equals(userId)) {
                return true;
            }
        }
        // 경로에 존재하는 허브 배송 담당자
        for (DeliveryRoute route : delivery.getDeliveryRoutes()) {
            if (route.getDeliveryManagerId() != null
                && route.getDeliveryManagerId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}
