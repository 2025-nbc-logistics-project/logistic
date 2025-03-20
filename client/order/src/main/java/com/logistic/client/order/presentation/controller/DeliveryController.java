package com.logistic.client.order.presentation.controller;

import com.logistic.client.order.application.dto.DeliveryResponseDto;
import com.logistic.client.order.application.dto.DeliverySearchDto;
import com.logistic.client.order.application.dto.DeliverySummaryDto;
import com.logistic.client.order.application.dto.PageResponseDto;
import com.logistic.client.order.application.service.DeliveryApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DeliveryController {

    private final DeliveryApplicationService deliveryApplicationService;

    @GetMapping("/v1/deliveries/{deliveryId}")
    public ResponseEntity<DeliveryResponseDto> readDelivery(@PathVariable("deliveryId") UUID deliveryId) {
        DeliveryResponseDto responseDto = deliveryApplicationService.readDelivery(deliveryId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/v1/deliveries/search")
    public ResponseEntity<PageResponseDto<DeliverySummaryDto>> searchDeliveries(@ModelAttribute DeliverySearchDto searchDto) {
        searchDto.validateSize(searchDto.getSize());
        PageResponseDto<DeliverySummaryDto> responseDtoPage = deliveryApplicationService.searchDeliveries(searchDto);
        return ResponseEntity.ok(responseDtoPage);
    }
}
