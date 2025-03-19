package com.logistic.client.order.application.service;

import com.logistic.client.order.application.dto.AddressResponse;
import com.logistic.client.order.application.dto.CreateDeliveryRequest;
import com.logistic.client.order.application.dto.HubRouteResponse;
import com.logistic.client.order.domain.model.*;
import com.logistic.client.order.domain.repository.DeliveryRepository;
import com.logistic.client.order.infrastructure.client.HubClient;
import com.logistic.client.order.infrastructure.client.SlackClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;

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
