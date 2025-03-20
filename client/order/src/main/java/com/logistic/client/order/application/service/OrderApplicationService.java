package com.logistic.client.order.application.service;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.domain.exception.OrderNotFoundException;
import com.logistic.client.order.domain.model.*;
import com.logistic.client.order.domain.repository.OrderRepository;
import com.logistic.client.order.domain.service.OrderDomainService;
import com.logistic.client.order.infrastructure.client.CompanyClient;
import com.logistic.client.order.infrastructure.client.SlackClient;
import com.logistic.client.order.presentation.request.OrderItemRequestDto;
import com.logistic.client.order.presentation.request.OrderRequestDto;
import com.logistic.client.order.presentation.request.OrderSearchDto;
import com.logistic.client.order.presentation.request.OrderUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {

    private final CompanyClient companyClient;
    private final SlackClient slackClient;
    private final DeliveryApplicationService deliveryApplicationService;
    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        /* (1).
        입력받은 상품들의 상품 Id가 유효한 지, 재고가 충분한 지 검증,
        문제가 없다면 입력받은 수량만큼 상품 재고 차감 요청,
        각 상품의 Id와 가격을 List 로 반환 받음
        */
        List<ProductPriceResponse> productPrices =
                companyClient.checkAndDeductStock(requestDto.getOrderItems());

        // (2). 입력받은 수령 업체 Id, 공급 업체 Id가 유효한지 검증을 요청하고 해당 업체들의 정보를 반환받음.
        CompanyResponse supplierResponse = companyClient.getCompany(requestDto.getSupplierCompanyId());
        CompanyResponse receiverResponse = companyClient.getCompany(requestDto.getReceiverCompanyId());

        // (3). (1)에서반환 받은 상품들의 가격을 토대로 List<OrderItem> 생성, 업체 Id로 CompanyInfo 생성
        List<OrderItem> orderItems =
            orderDomainService.buildOrderItems(requestDto.getOrderItems(), productPrices);
        CompanyInfo companyInfo = new CompanyInfo(supplierResponse.getCompanyId(), receiverResponse.getCompanyId());


        // (4). Order 엔티티 생성 및 DB 저장
        Order order = new Order(
            companyInfo,
            orderItems,
            requestDto.getOrderRequest()
        );
        orderRepository.save(order);

        // (5). 배송 엔티티를 생성하기 위해 필요한 Request 데이터 생성
        CreateDeliveryRequest createDeliveryRequest = new CreateDeliveryRequest(
            order.getOrderId(),
            supplierResponse.getHubId(),
            supplierResponse.getDeliveryManagerId(),
            supplierResponse.getAddress(),
            receiverResponse.getHubId(),
            receiverResponse.getDeliveryManagerId(),
            receiverResponse.getAddress()
        );

        // (6). 배송 엔티티 생성 요청
        deliveryApplicationService.createDelivery(createDeliveryRequest);
        Delivery delivery = deliveryApplicationService.createDelivery(createDeliveryRequest); // 나중에 ResponseDto로 수정하자

        // (7). 배송 데이터 업데이트
        order.addDelivery(delivery.getDeliveryId());

        // (8). 슬랙 메시지 생성 요청
        SlackRequestDto slackRequestDto = orderDomainService.buildSlackMessageRequest(
            order,
            delivery,
            orderItems
        );
        slackClient.createSlackMessage(slackRequestDto);

        return new OrderResponseDto(order);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto readOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        return new OrderResponseDto(order);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<OrderSummaryDto> searchOrders(OrderSearchDto searchDto) {
        Page<OrderSummaryDto> mappedPage = orderRepository.searchOrders(searchDto).map(OrderSummaryDto::new);
        return new PageResponseDto<>(mappedPage);
    }

    @Transactional
    public OrderResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto requestDto) {

        // (1). 기존 Order 조회 및 수정 가능 여부 확인 (배송 중이면 X)
        Order order = findOrderById(orderId);
        order.checkEditable();

        // (2). 도메인 서비스를 호출하여 기존 orderItems 와 요청 orderItems 의 차이(변경된 수량, 없어지거나 추가된 Item 등) 계산
        ItemDifference diff = orderDomainService.calculateItemDifference(order.getOrderItems(), requestDto.getOrderItems());

        // (3). 줄어든(없어진) 상품 재고 복원 요청
        if (!diff.getRestoreList().isEmpty()) {
            companyClient.restoreStock(diff.getRestoreList());
        }

        // (4). 늘어난(추가된) 상품 재고 차감 요청, 새로운 Price 맵에 저장
        Map<UUID, Integer> updatedPriceMap = new HashMap<>();
        if (!diff.getDeductList().isEmpty()) {
            List<ProductPriceResponse> productPrices =
                companyClient.checkAndDeductStock(diff.getDeductList());

            for (ProductPriceResponse ppr : productPrices) {
                updatedPriceMap.put(ppr.getProductId(), ppr.getPrice());
            }
        }


        // (5). 도메인 서비스를 호출하여 최종적으로 새로운 OrderItem 목록을 재구성
        List<OrderItem> newOrderItems
            = orderDomainService.buildUpdatedOrderItems(order.getOrderItems(), requestDto.getOrderItems(), updatedPriceMap);

        // (6). Order 도메인에 최종 업데이트
        order.updateItems(newOrderItems);
        if (requestDto.getOrderRequest() != null) {
            order.updateRequest(requestDto.getOrderRequest());
        }

        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);

        order.updateStatus(newStatus);

        return new OrderResponseDto(order);
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        order.checkEditable();

        List<OrderItemRequestDto> restoreList = order.getOrderItems().stream()
                .map(oi -> new OrderItemRequestDto(
                    oi.getProductId(),
                    oi.getQuantity().getQuantity()
                ))
                .toList();

        if (!restoreList.isEmpty()) {
            companyClient.restoreStock(restoreList);
        }

        if (order.getDeliveryId() != null) {
            deliveryApplicationService.deleteDelivery(order.getDeliveryId());
        }
        order.markAsDeleted(1L); // TODO : 실제 유저 Id 추가
    }

    private Order findOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("해당 Id를 가진 주문 정보를 찾지 못했습니다."));
    }
}
