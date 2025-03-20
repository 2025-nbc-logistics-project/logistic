package com.logistic.client.order.application.service;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.domain.model.*;
import com.logistic.client.order.domain.repository.OrderRepository;
import com.logistic.client.order.infrastructure.client.CompanyClient;
import com.logistic.client.order.infrastructure.client.SlackClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {

    private final CompanyClient companyClient;
    private final SlackClient slackClient;
    private final DeliveryApplicationService deliveryApplicationService;
    private final OrderRepository orderRepository;

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
            buildOrderItems(requestDto.getOrderItems(), productPrices);
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
//            buildShippingInfo(supplierResponse.getAddress(), receiverResponse.getAddress())
        );

        // (6). 배송 엔티티 생성 요청
        deliveryApplicationService.createDelivery(createDeliveryRequest);
        Delivery delivery = deliveryApplicationService.createDelivery(createDeliveryRequest); // 나중에 ResponseDto로 수정하자

        // (7). 배송 데이터 업데이트
        order.addDelivery(delivery.getDeliveryId());

        // (8). 슬랙 메시지 생성 요청
        SlackRequestDto slackRequestDto = buildSlackMessageRequest(
            order,
            delivery,
            orderItems
        );
        slackClient.createSlackMessage(slackRequestDto);

        return new OrderResponseDto(order);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto readOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 주문 정보를 찾지 못했습니다."));
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
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 주문 정보를 찾지 못했습니다."));
        order.checkEditable();

        // (2). 기존 OrderItem 을 맵으로 변환
        Map<UUID, Integer> oldQtyMap = new HashMap<>();
        Map<UUID, Integer> oldPriceMap = new HashMap<>();
        for (OrderItem oi : order.getOrderItems()) {
            oldQtyMap.put(oi.getProductId(), oi.getQuantity().getQuantity());
            oldPriceMap.put(oi.getProductId(), oi.getPrice().getAmount());
        }

        // (3). 새로 들어온 요청 Item 을 맵으로 구성
        Map<UUID, Integer> newQtyMap = requestDto.getOrderItems().stream()
            .collect(Collectors.toMap(
                OrderItemRequestDto::getProductId,
                OrderItemRequestDto::getQuantity
            ));

        // (4). old + new 전체 productId 합집합을 대상으로 diff 계산
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

        // (5). 줄어든(없어진) 상품 재고 복원 요청
        if (!restoreList.isEmpty()) {
            companyClient.restoreStock(restoreList);
        }

        // (6). 늘어난(추가된) 상품 재고 차감 요청, 새로운 Price 맵에 저장
        Map<UUID, Integer> updatedPriceMap = new HashMap<>();
        if (!deductList.isEmpty()) {
            List<ProductPriceResponse> productPrices =
                companyClient.checkAndDeductStock(deductList);

            for (ProductPriceResponse ppr : productPrices) {
                updatedPriceMap.put(ppr.getProductId(), ppr.getPrice());
            }
        }

        /*
        (7). 최종적으로 새로운 OrderItem 목록을 재구성
             - 입력된 productId가 updatePriceMap 에 있다면 거기서 price 를 꺼내고, 없으면(oldPriceMap 에 있으면) 그대로 사용
         */
        List<OrderItem> newOrderItems = new ArrayList<>();
        for (Map.Entry<UUID, Integer> entry : newQtyMap.entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();

            Integer newPrice = updatedPriceMap.get(productId);
            if (newPrice == null) {
                newPrice = oldPriceMap.get(productId);
            }

            newOrderItems.add(
                new OrderItem(
                    productId,
                    new Quantity(quantity),
                    new Money(newPrice)
                )
            );
        }

        // (8). Order 도메인에 최종 업데이트
        order.updateItems(newOrderItems);
        if (requestDto.getOrderRequest() != null) {
            order.updateRequest(requestDto.getOrderRequest());
        }

        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 주문 정보를 찾지 못했습니다."));

        order.updateStatus(newStatus);

        return new OrderResponseDto(order);
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 주문 정보를 찾지 못했습니다."));
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

    private List<OrderItem> buildOrderItems(
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

    private SlackRequestDto buildSlackMessageRequest(Order order, Delivery delivery, List<OrderItem> orderItems) {
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
}
