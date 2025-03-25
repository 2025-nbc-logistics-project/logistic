package com.logistic.client.order.presentation.controller;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.application.service.OrderApplicationService;
import com.logistic.client.order.domain.model.OrderStatus;
import com.logistic.client.order.presentation.request.OrderRequestDto;
import com.logistic.client.order.presentation.request.OrderSearchDto;
import com.logistic.client.order.presentation.request.OrderUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Order API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @PostMapping("")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto,
                                                        HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        String username = request.getHeader("username");
        UUID userId = UUID.fromString(userIdStr);

        OrderResponseDto responseDto = orderApplicationService.createOrder(requestDto, userId, username);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "주문 단일 조회",
        description = "주문 ID로 특정 주문의 상세 정보를 조회합니다."
    )
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> readOrder(@PathVariable("orderId") UUID orderId,
                                                      HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        OrderResponseDto responseDto = orderApplicationService.readOrder(orderId, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "주문 검색",
        description = "업체 ID를 기준으로 주문을 검색합니다."
    )
    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<OrderSummaryDto>> searchOrders(@ModelAttribute OrderSearchDto searchDto,
                                                                         HttpServletRequest request) {
        searchDto.validateSize(searchDto.getSize());
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        PageResponseDto<OrderSummaryDto> responseDtoPage = orderApplicationService.searchOrders(searchDto, userId, role);
        return ResponseEntity.ok(responseDtoPage);
    }

    @Operation(
        summary = "주문 수정",
        description = "주문 요청사항, 주문 상품을 수정합니다. (배송이 시작되었다면 불가능)"
    )
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable("orderId") UUID orderId,
                                                        @RequestBody OrderUpdateRequestDto requestDto,
                                                        HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        OrderResponseDto responseDto = orderApplicationService.updateOrder(orderId, requestDto, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "주문 상태 업데이트 (Feign)",
        description = "배송 상태의 변화에 따라 주문 상태를 업데이트합니다."
    )
    @PatchMapping("/status/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable("orderId") UUID orderId,
                                                              @RequestParam("newStatus") String statusString) {
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("잘못된 OrderStatus 입니다.");
        }
        OrderResponseDto responseDto = orderApplicationService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "주문 삭제", description = "주문을 논리적 삭제합니다.")
    @PatchMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") UUID orderId, HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        orderApplicationService.deleteOrder(orderId, userId, role);
        return ResponseEntity.ok("삭제 성공 : " + orderId);
    }

}
