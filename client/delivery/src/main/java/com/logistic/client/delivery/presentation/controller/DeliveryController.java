package com.logistic.client.delivery.presentation.controller;

import com.logistic.client.delivery.application.dto.*;
import com.logistic.client.delivery.application.service.DeliveryApplicationService;
import com.logistic.client.delivery.domain.model.Delivery;
import com.logistic.client.delivery.domain.model.DeliveryRouteStatus;
import com.logistic.client.delivery.domain.model.DeliveryStatus;
import com.logistic.client.delivery.presentation.request.DeliverySearchDto;
import com.logistic.client.delivery.presentation.request.DeliveryUpdateRequestDto;
import com.logistic.client.delivery.presentation.request.routeUpdateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryApplicationService deliveryApplicationService;

    @PostMapping("")
    public FeignDeliveryResponse createDelivery(@RequestBody CreateDeliveryRequest requestDto) {

        return deliveryApplicationService.createDelivery(requestDto);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponseDto> readDelivery(@PathVariable("deliveryId") UUID deliveryId,
                                                            HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        DeliveryResponseDto responseDto = deliveryApplicationService.readDelivery(deliveryId, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<DeliverySummaryDto>> searchDeliveries(@ModelAttribute DeliverySearchDto searchDto,
                                                                                HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        searchDto.validateSize(searchDto.getSize());
        PageResponseDto<DeliverySummaryDto> responseDtoPage
            = deliveryApplicationService.searchDeliveries(searchDto, userId, role);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliverySummaryDto> updateDelivery(@PathVariable("deliveryId") UUID deliveryId,
                                                             @RequestBody DeliveryUpdateRequestDto requestDto,
                                                             HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        DeliverySummaryDto responseDto
            = deliveryApplicationService.updateDelivery(deliveryId, requestDto, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/status/{deliveryId}")
    public ResponseEntity<DeliverySummaryDto> updateDeliveryStatus(@PathVariable("deliveryId") UUID deliveryId,
                                                                   @RequestParam("newStatus") DeliveryStatus newStatus,
                                                                   HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        DeliverySummaryDto responseDto
            = deliveryApplicationService.updateDeliveryStatus(deliveryId, newStatus, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{deliveryId}/routes/next")
    public ResponseEntity<DeliveryRouteDto> updateNextRouteStatus(@PathVariable UUID deliveryId,
                                                                  @RequestParam("newStatus") DeliveryRouteStatus newStatus,
                                                                  HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        DeliveryRouteDto responseDto
            = deliveryApplicationService.updateNextRouteStatus(deliveryId, newStatus, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{deliveryId}/routes/{routeId}")
    public ResponseEntity<DeliveryRouteDto> updateActualDistanceTime(@PathVariable UUID deliveryId,
                                                                     @PathVariable UUID routeId,
                                                                     @RequestBody routeUpdateRequestDto requestDto,
                                                                     HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        DeliveryRouteDto responseDto
            = deliveryApplicationService.updateActualDistanceTime(deliveryId, routeId, requestDto, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{deliveryId}")
    public ResponseEntity<String> deleteDelivery(@PathVariable("deliveryId") UUID deliveryId,
                                                 HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        deliveryApplicationService.deleteDelivery(deliveryId, userId, role);
        return ResponseEntity.ok("삭제 성공 : " + deliveryId);
    }

}
