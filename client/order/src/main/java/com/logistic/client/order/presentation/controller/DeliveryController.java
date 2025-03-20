package com.logistic.client.order.presentation.controller;

import com.logistic.client.order.application.dto.*;
import com.logistic.client.order.application.service.DeliveryApplicationService;
import com.logistic.client.order.domain.model.DeliveryRouteStatus;
import com.logistic.client.order.domain.model.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryApplicationService deliveryApplicationService;

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponseDto> readDelivery(@PathVariable("deliveryId") UUID deliveryId) {
        DeliveryResponseDto responseDto = deliveryApplicationService.readDelivery(deliveryId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<DeliverySummaryDto>> searchDeliveries(@ModelAttribute DeliverySearchDto searchDto) {
        searchDto.validateSize(searchDto.getSize());
        PageResponseDto<DeliverySummaryDto> responseDtoPage = deliveryApplicationService.searchDeliveries(searchDto);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliverySummaryDto> updateDelivery(@PathVariable("deliveryId") UUID deliveryId,
                                                              @RequestBody DeliveryUpdateRequestDto requestDto) {
        DeliverySummaryDto responseDto = deliveryApplicationService.updateDelivery(deliveryId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/status/{deliveryId}")
    public ResponseEntity<DeliverySummaryDto> updateDeliveryStatus(@PathVariable("deliveryId") UUID deliveryId,
                                                                    @RequestParam("newStatus") DeliveryStatus newStatus) {
        DeliverySummaryDto responseDto = deliveryApplicationService.updateDeliveryStatus(deliveryId, newStatus);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{deliveryId}/routes/next")
    public ResponseEntity<DeliveryRouteDto> updateNextRouteStatus(@PathVariable UUID deliveryId,
                                                                  @RequestParam("newStatus") DeliveryRouteStatus newStatus) {
        DeliveryRouteDto responseDto = deliveryApplicationService.updateNextRouteStatus(deliveryId, newStatus);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{deliveryId}/routes/{routeId}")
    public ResponseEntity<DeliveryRouteDto> updateActualDistanceTime(@PathVariable UUID deliveryId,
                                                                     @PathVariable UUID routeId,
                                                                     @RequestBody routeUpdateRequestDto requestDto) {
        DeliveryRouteDto responseDto = deliveryApplicationService.updateActualDistanceTime(deliveryId, routeId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{deliveryId}")
    public ResponseEntity<String> deleteDelivery(@PathVariable("deliveryId") UUID deliveryId) {
        deliveryApplicationService.deleteDelivery(deliveryId);
        return ResponseEntity.ok("삭제 성공 : " + deliveryId);
    }

}
