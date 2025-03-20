package com.logistic.client.order.domain.service;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.domain.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderDomainService {
    public List<OrderItem> buildOrderItems(
        List<OrderItemRequestDto> orderItems,
        List<ProductPriceResponse> productPrices
    ) {
        Map<UUID, Integer> priceMap = productPrices.stream()
            .collect(Collectors.toMap(
                ProductPriceResponse::getProductId,
                ProductPriceResponse::getPrice
            ));

        return orderItems.stream()
            .map(dto -> {
                UUID productId = dto.getProductId();
                Integer price = priceMap.get(productId);
                if (price == null) {
                    throw new IllegalArgumentException("상품의 정보가 올바르지 않습니다 : " +  productId);
                }
                return new OrderItem(
                    productId,
                    new Quantity(dto.getQuantity()),
                    new Money(price)
                );
            })
            .collect(Collectors.toList());
    }

    public SlackRequestDto buildSlackMessageRequest(Order order, Delivery delivery, List<OrderItem> orderItems) {
        // 주문자 정보 (User 도메인이 구현되면 반영할 예정)
        String username = "mock username";
        String email = "mock@email.com";

        // 주문 상품 목록
        List<SlackOrderItem> slackOrderItems = orderItems.stream()
            .map(oi -> new SlackOrderItem(
                oi.getProductId(),
                oi.getQuantity().getQuantity()
            ))
            .toList();

        // 경유 허브 Id 리스트
        List<UUID> transitHubs = delivery.getDeliveryRoutes().stream()
            .map(route -> route.getDeliveryHubInfo().getDestinationHubId())
            .toList();

        // 수령 업체의 주소
        Address receiverAddress = delivery.getShippingInfo().getReceiverAddress();
        AddressResponse destinationAddress = new AddressResponse(
            receiverAddress.getPostalCode(),
            receiverAddress.getDetailAddress(),
            receiverAddress.getStreetAddress()
        );

        return new SlackRequestDto(
            order.getOrderId(),
            username,
            email,
            slackOrderItems,
            order.getOrderRequest(),
            delivery.getDeliveryHubInfo().getDepartureHubId(),
            transitHubs,
            destinationAddress,
            order.getCompanyInfo().getSupplierCompanyId()
        );
    }

    public ItemDifference calculateItemDifference(List<OrderItem> orderItems, List<OrderItemRequestDto> newItemRequests) {
        // 기존 OrderItem 을 맵으로 변환
        Map<UUID, Integer> oldQtyMap = new HashMap<>();
        for (OrderItem oi : orderItems) {
            oldQtyMap.put(oi.getProductId(), oi.getQuantity().getQuantity());
        }

        // 새로 들어온 요청 Item 을 맵으로 구성
        Map<UUID, Integer> newQtyMap = toQtyMap(newItemRequests);

        // old + new 전체 productId 합집합을 대상으로 diff 계산
        Set<UUID> ProductIds = new HashSet<>();
        ProductIds.addAll(oldQtyMap.keySet());
        ProductIds.addAll(newQtyMap.keySet());

        List<OrderItemRequestDto> restoreList = new ArrayList<>();
        List<OrderItemRequestDto> deductList = new ArrayList<>();

        for (UUID productId : ProductIds) {
            int oldQty = oldQtyMap.getOrDefault(productId, 0);
            int newQty = newQtyMap.getOrDefault(productId, 0);
            int diff = newQty - oldQty;

            if (diff > 0) {
                // 새로 늘어난 만큼 차감 필요
                deductList.add(new OrderItemRequestDto(productId, diff));
            } else if (diff < 0) {
                // 줄어든 만큼 복원 필요
                restoreList.add(new OrderItemRequestDto(productId, Math.abs(diff)));
            }
        }

        return new ItemDifference(restoreList, deductList);
    }

    public List<OrderItem> buildUpdatedOrderItems(
        List<OrderItem> orderItems,
        List<OrderItemRequestDto> newItemRequests,
        Map<UUID, Integer> updatedPriceMap
    ) {
        // 기존 orderItems 를 PriceMap 으로 변환
        Map<UUID, Integer> oldPriceMap = new HashMap<>();
        for (OrderItem oi : orderItems) {
            oldPriceMap.put(oi.getProductId(), oi.getPrice().getAmount());
        }

        // 입력된 productId가 updatePriceMap 에 있다면 거기서 price 를 꺼내고, 없으면(oldPriceMap 에 있으면) 그대로 사용
        List<OrderItem> newOrderItems = new ArrayList<>();
        Map<UUID, Integer> newQtyMap = toQtyMap(newItemRequests);
        for (Map.Entry<UUID, Integer> entry : newQtyMap.entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();

            Integer newPrice = updatedPriceMap.get(productId);
            if (newPrice == null) {
                newPrice = oldPriceMap.get(productId);
            }
            newOrderItems.add(new OrderItem(productId, new Quantity(quantity), new Money(newPrice)));
        }
        return newOrderItems;
    }

    private Map<UUID, Integer> toQtyMap(List<OrderItemRequestDto> newItemRequests) {
        return newItemRequests.stream()
            .collect(Collectors.toMap(
                OrderItemRequestDto::getProductId,
                OrderItemRequestDto::getQuantity
            ));
    }
}
