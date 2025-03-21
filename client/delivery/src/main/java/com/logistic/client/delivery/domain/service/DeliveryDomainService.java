package com.logistic.client.delivery.domain.service;

import com.logistic.client.delivery.application.dto.AddressResponse;
import com.logistic.client.delivery.application.dto.HubRouteResponse;
import com.logistic.client.delivery.domain.exception.RouteAlreadyArrivedException;
import com.logistic.client.delivery.domain.model.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryDomainService {

    public void addRoutesToDelivery(Delivery delivery, List<HubRouteResponse> routeResponses) {
        for (HubRouteResponse routeResponse : routeResponses) {
            DeliveryRoute route = new DeliveryRoute(
                routeResponse.getSequence(),
                new DeliveryHubInfo(routeResponse.getDepartureHubId(), routeResponse.getDestinationHubId()),
                new DistanceTime(routeResponse.getDistance(), routeResponse.getTime()),
                routeResponse.getDeliveryManagerId()
            );
            delivery.addRoute(route);
        }
    }

    public Address buildNewAddress(Address oldAddress, String postalCode, String detailAddress, String streetAddress) {
        return new Address(
            (postalCode != null) ? postalCode : oldAddress.getPostalCode(),
            (detailAddress != null) ? detailAddress : oldAddress.getDetailAddress(),
            (streetAddress != null) ? streetAddress : oldAddress.getStreetAddress()
        );
    }

    public DeliveryRoute findNextRouteToUpdate(Delivery delivery) {
        // routes 를 sequence 오름차순으로 정렬
        List<DeliveryRoute> sortedRoutes = delivery.getDeliveryRoutes().stream()
            .sorted(Comparator.comparingInt(DeliveryRoute::getSequence))
            .toList();

        // 배송이 완료되지 않은 route 중 첫 번째 (가장 작은 sequence) 찾기
        DeliveryRoute targetRoute = null;
        for (DeliveryRoute route : sortedRoutes) {
            if (route.getRouteStatus() != DeliveryRouteStatus.HUB_ARRIVED) {
                targetRoute = route;
                break;
            }
        }

        // 모든 경로가 배송 완료되었다면, 예외 처리
        if (targetRoute == null) {
            throw new RouteAlreadyArrivedException("이미 모든 경로가 배송 완료되어, 업데이트할 route 가 없습니다.");
        }

        return targetRoute;
    }

    public void updateActualDistance(DeliveryRoute route, Integer distance, Integer time) {
        if (route.getRouteStatus() != DeliveryRouteStatus.HUB_ARRIVED) {
            throw new IllegalStateException("아직 배송이 끝나지 않은 경로이므로, 실제 거리/시간을 업데이트 할 수 없습니다.");
        }
        route.updateActualDistanceTime(distance, time);
    }

    public ShippingInfo buildShippingInfo(AddressResponse receiver, AddressResponse supplier) {
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
