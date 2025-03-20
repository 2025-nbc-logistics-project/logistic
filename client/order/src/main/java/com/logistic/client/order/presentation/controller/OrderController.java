package com.logistic.client.order.presentation.controller;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.application.service.OrderApplicationService;
import com.logistic.client.order.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping("/v1/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderApplicationService.createOrder(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/v1/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> readOrder(@PathVariable("orderId") UUID orderId) {
        OrderResponseDto responseDto = orderApplicationService.readOrder(orderId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/v1/orders/search")
    public ResponseEntity<PageResponseDto<OrderSummaryDto>> searchOrders(@ModelAttribute OrderSearchDto searchDto) {
        searchDto.validateSize(searchDto.getSize());
        PageResponseDto<OrderSummaryDto> responseDtoPage = orderApplicationService.searchOrders(searchDto);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PutMapping("/v1/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable("orderId") UUID orderId, @RequestBody OrderUpdateRequestDto requestDto) {
        OrderResponseDto responseDto = orderApplicationService.updateOrder(orderId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/v1/orders/status/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable("orderId") UUID orderId, @RequestParam("newStatus") OrderStatus newStatus) {
        OrderResponseDto responseDto = orderApplicationService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/v1/orders/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") UUID orderId) {
        orderApplicationService.deleteOrder(orderId);
        return ResponseEntity.ok("삭제 성공 : " + orderId);
    }

}
