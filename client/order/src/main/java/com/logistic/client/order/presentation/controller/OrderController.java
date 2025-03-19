package com.logistic.client.order.presentation.controller;

import com.logistic.client.order.application.dto.OrderRequestDto;
import com.logistic.client.order.application.dto.OrderResponseDto;
import com.logistic.client.order.application.dto.OrderSearchDto;
import com.logistic.client.order.application.dto.PageResponseDto;
import com.logistic.client.order.application.service.OrderApplicationService;
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
    public ResponseEntity<PageResponseDto<OrderResponseDto>> searchOrders(@ModelAttribute OrderSearchDto searchDto) {
        searchDto.validateSize(searchDto.getSize());
        PageResponseDto<OrderResponseDto> responseDtoPage = orderApplicationService.searchOrders(searchDto);
        return ResponseEntity.ok(responseDtoPage);
    }

}
