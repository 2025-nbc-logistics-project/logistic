package com.logistic.client.order.presentation.controller;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.application.service.OrderApplicationService;
import com.logistic.client.order.domain.model.OrderStatus;
import com.logistic.client.order.presentation.request.OrderRequestDto;
import com.logistic.client.order.presentation.request.OrderSearchDto;
import com.logistic.client.order.presentation.request.OrderUpdateRequestDto;
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
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderApplicationService.createOrder(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> readOrder(@PathVariable("orderId") UUID orderId) {
        OrderResponseDto responseDto = orderApplicationService.readOrder(orderId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<OrderSummaryDto>> searchOrders(@ModelAttribute OrderSearchDto searchDto) {
        searchDto.validateSize(searchDto.getSize());
        PageResponseDto<OrderSummaryDto> responseDtoPage = orderApplicationService.searchOrders(searchDto);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable("orderId") UUID orderId, @RequestBody OrderUpdateRequestDto requestDto) {
        OrderResponseDto responseDto = orderApplicationService.updateOrder(orderId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable("orderId") UUID orderId, @RequestParam("newStatus") OrderStatus newStatus) {
        OrderResponseDto responseDto = orderApplicationService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") UUID orderId) {
        orderApplicationService.deleteOrder(orderId);
        return ResponseEntity.ok("삭제 성공 : " + orderId);
    }

}
