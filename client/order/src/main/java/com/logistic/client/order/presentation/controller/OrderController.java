package com.logistic.client.order.presentation.controller;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.application.service.OrderApplicationService;
import com.logistic.client.order.domain.model.OrderStatus;
import com.logistic.client.order.presentation.request.OrderRequestDto;
import com.logistic.client.order.presentation.request.OrderSearchDto;
import com.logistic.client.order.presentation.request.OrderUpdateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping("")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto,
                                                        HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        String username = request.getHeader("username");
        UUID userId = UUID.fromString(userIdStr);

        OrderResponseDto responseDto = orderApplicationService.createOrder(requestDto, userId, username);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> readOrder(@PathVariable("orderId") UUID orderId,
                                                      HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        OrderResponseDto responseDto = orderApplicationService.readOrder(orderId, userId, role);
        return ResponseEntity.ok(responseDto);
    }

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

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable("orderId") UUID orderId,
                                                              @RequestParam("newStatus") String statusString,
                                                              HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("잘못된 OrderStatus 입니다.");
        }
        OrderResponseDto responseDto = orderApplicationService.updateOrderStatus(orderId, newStatus, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") UUID orderId, HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        orderApplicationService.deleteOrder(orderId, userId, role);
        return ResponseEntity.ok("삭제 성공 : " + orderId);
    }

}
