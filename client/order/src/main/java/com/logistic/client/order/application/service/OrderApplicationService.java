package com.logistic.client.order.application.service;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.domain.model.*;
import com.logistic.client.order.domain.repository.OrderRepository;
import com.logistic.client.order.infrastructure.client.CompanyClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {

    private final CompanyClient companyClient;
    private final DeliveryApplicationService deliveryApplicationService;
    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(OrderRequestDto requestDto) {
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

        return order;
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
}
