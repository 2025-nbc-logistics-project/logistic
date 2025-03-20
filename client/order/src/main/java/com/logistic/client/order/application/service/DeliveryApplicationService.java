package com.logistic.client.order.application.service;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.domain.model.*;
import com.logistic.client.order.domain.repository.DeliveryRepository;
import com.logistic.client.order.infrastructure.client.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;
    private final OrderApplicationService orderApplicationService;

    public Delivery createDelivery(CreateDeliveryRequest request) {

        // (1). request 를 기반으로 delivery 엔티티 생성
        Delivery delivery = new Delivery(
            request.getOrderId(),
            new DeliveryManagerId(request.getReceiverDeliveryManager(), request.getSupplierDeliveryManager()),
            new DeliveryHubInfo(request.getDepartureHubId(), request.getDestinationHubId()),
            buildShippingInfo(request.getReceiverAddress(), request.getSupplierAddress())
        );

        // (2). Hub 서비스를 호출하여 출발 허브부터 최종 목적지 허브까지의 경로 계산 요청, 각 허브 간의 이동 경로를 List 로 반환 받음
        List<HubRouteResponse> routeResponses = hubClient.getHubRoutes(
            request.getDepartureHubId(),
            request.getDestinationHubId()
        );

        // (3). 허브 간의 이동 경로 List 를 기반으로 DeliveryRoute 생성 및 delivery 엔티티에 추가
        for (HubRouteResponse routeResponse : routeResponses) {
            DeliveryRoute route = new DeliveryRoute(
                routeResponse.getSequence(),
                new DeliveryHubInfo(routeResponse.getDepartureHubId(), routeResponse.getDestinationHubId()),
                new DistanceTime(routeResponse.getDistance(), routeResponse.getTime()),
                routeResponse.getDeliveryManagerId()
            );
            delivery.addRoute(route);
        }

        // (4). DB에 저장
        deliveryRepository.save(delivery);

        // 이벤트 발행 또는 Order 서비스를 호출하여 배송 데이터 업데이트 및 슬랙 메시지 발송 요청 (추후 반영 예정)
        return delivery;
    }

    @Transactional(readOnly = true)
    public DeliveryResponseDto readDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 Delivery가 존재하지 않습니다."));

        return new DeliveryResponseDto(delivery);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<DeliverySummaryDto> searchDeliveries(DeliverySearchDto searchDto) {
        Page<DeliverySummaryDto> mappedPage = deliveryRepository.searchDeliveries(searchDto).map(DeliverySummaryDto::new);
        return new PageResponseDto<>(mappedPage);
    }

    @Transactional
    public DeliverySummaryDto updateDelivery(UUID deliveryId, DeliveryUpdateRequestDto requestDto) {

        // (1). 배송 조회
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 Delivery가 존재하지 않습니다."));

        // (2). 변경할 수령 업체 배송 담당자, 공급 업체 배송 담당자가 입력되었다면 Update
        if (requestDto.getReceiverDeliveryManagerId() != null) {
            delivery.updateReceiverManager(requestDto.getReceiverDeliveryManagerId());
        }
        if (requestDto.getSupplierDeliveryManagerId() != null) {
            delivery.updateSupplierManager(requestDto.getSupplierDeliveryManagerId());
        }

        // (3). 수정할 ShippingInfo 데이터가 입력되었다면 반영, 입력되지 않았다면 기존 데이터 사용
        ShippingInfo oldShipping = delivery.getShippingInfo();

        Address newReceiverAddress = new Address(
            requestDto.getReceiverPostalCode() != null ? requestDto.getReceiverPostalCode() : oldShipping.getReceiverAddress().getPostalCode(),
            requestDto.getReceiverDetailAddress() != null ? requestDto.getReceiverDetailAddress() : oldShipping.getReceiverAddress().getDetailAddress(),
            requestDto.getReceiverStreetAddress() != null ? requestDto.getReceiverStreetAddress() : oldShipping.getReceiverAddress().getStreetAddress()
        );

        String newRecipientName = requestDto.getRecipientName() != null ? requestDto.getRecipientName() : oldShipping.getRecipientName();
        UUID newRecipientSlackId = requestDto.getRecipientSlackId() != null ? requestDto.getRecipientSlackId() : oldShipping.getRecipientSlackId();

        // (4). 새 ShippingInfo 로 Update
        ShippingInfo newShipping = new ShippingInfo(newReceiverAddress, oldShipping.getSupplierAddress(), newRecipientName, newRecipientSlackId);
        delivery.updateShippingInfo(newShipping);

        return new DeliverySummaryDto(delivery);
    }

    @Transactional
    public DeliverySummaryDto updateDeliveryStatus(UUID deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 Delivery가 존재하지 않습니다."));

        delivery.updateStatus(newStatus);

        OrderStatus newOrderStatus = deriveOrderStatus(delivery.getStatus());
        if (newOrderStatus != null) { // TODO : feign 호출 또는 이벤트 방식으로 변경 예정
            orderApplicationService.updateOrderStatus(delivery.getOrderId(), newOrderStatus);
        }

        return new DeliverySummaryDto(delivery);
    }

    @Transactional
    public DeliveryRouteDto updateNextRouteStatus(UUID deliveryId, DeliveryRouteStatus newStatus) {
        // (1). 배송 조회
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 Delivery가 존재하지 않습니다."));

        // (2). routes 를 sequence 오름차순으로 정렬
        List<DeliveryRoute> sortedRoutes = delivery.getDeliveryRoutes().stream()
            .sorted(Comparator.comparingInt(DeliveryRoute::getSequence))
            .toList();

        // (3). 배송이 완료되지 않은 route 중 첫 번째 (가장 작은 sequence) 찾기
        DeliveryRoute targetRoute = null;
        for (DeliveryRoute route : sortedRoutes) {
            if (route.getRouteStatus() != DeliveryRouteStatus.HUB_ARRIVED) {
                targetRoute = route;
                break;
            }
        }

        // (4). 모든 경로가 배송 완료되었다면, 예외 처리
        if (targetRoute == null) {
            throw new IllegalArgumentException("이미 모든 경로가 배송 완료되어, 업데이트할 route 가 없습니다.");
        }

        // (5). 상태 업데이트
        targetRoute.updateRouteStatus(newStatus);

        return new DeliveryRouteDto(targetRoute);
    }

    public DeliveryRouteDto updateActualDistanceTime(UUID deliveryId, UUID routeId, routeUpdateRequestDto requestDto) {
        // (1). Delivery 조회
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 Delivery가 존재하지 않습니다."));

        // (2). routeId에 해당하는 DeliveryRoute 찾기
        DeliveryRoute targetRoute = delivery.getDeliveryRoutes().stream()
            .filter(r -> r.getDeliveryRouteId().equals(routeId))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 Route가 존재하지 않습니다."));

        // (3). route의 상태가 HUB_ARRIVED 가 아니면 예외 처리
        if (targetRoute.getRouteStatus() != DeliveryRouteStatus.HUB_ARRIVED) {
            throw new IllegalArgumentException("아직 배송이 끝나지 않은 경로이므로, 실제 거리/시간을 업데이트 할 수 없습니다.");
        }

        // (4). 실제 거리/소요시간 업데이트
        targetRoute.updateActualDistanceTime(requestDto.getDistance(), requestDto.getTime());

        return new DeliveryRouteDto(targetRoute);
    }

    @Transactional
    public void deleteDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 Delivery가 존재하지 않습니다."));

        delivery.markAsDeleted(1L); // Todo : 실제 유저 Id 추가
    }

    private OrderStatus deriveOrderStatus(DeliveryStatus status) {
        switch (status) {
            case WAITING_AT_HUB -> { return OrderStatus.DELIVERING; }
            case DELIVERY_COMPLETED -> { return OrderStatus.COMPLETED; }
            default -> { return null; }
        }
    }

    private ShippingInfo buildShippingInfo(AddressResponse receiver, AddressResponse supplier) {
        Address receiverAddress = new Address(
            receiver.getPostalCode(),
            receiver.getDetailAddress(),
            receiver.getStreetAddress()
        );
        Address supplierAddress = new Address(
            supplier.getPostalCode(),
            supplier.getDetailAddress(),
            supplier.getStreetAddress()
        );

        // recipientName, slackId는 어떻게 받아올 지 고민좀..
        return new ShippingInfo(receiverAddress, supplierAddress, "수령인", UUID.randomUUID());
    }
}
