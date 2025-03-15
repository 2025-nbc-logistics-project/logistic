package com.logistic.client.order.presentation.controller;

import com.logistic.client.order.application.dto.OrderRequestDto;
import com.logistic.client.order.application.dto.OrderResponseDto;
import com.logistic.client.order.application.service.OrderApplicationService;
import com.logistic.client.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping("/v1/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto) {
        Order order = orderApplicationService.createOrder(requestDto);
        return ResponseEntity.ok(new OrderResponseDto(order));
    }

}
