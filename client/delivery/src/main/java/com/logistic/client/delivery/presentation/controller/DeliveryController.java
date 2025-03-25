package com.logistic.client.delivery.presentation.controller;

import com.logistic.client.delivery.application.dto.*;
import com.logistic.client.delivery.application.service.DeliveryApplicationService;
import com.logistic.client.delivery.domain.model.Delivery;
import com.logistic.client.delivery.domain.model.DeliveryRouteStatus;
import com.logistic.client.delivery.domain.model.DeliveryStatus;
import com.logistic.client.delivery.presentation.request.DeliverySearchDto;
import com.logistic.client.delivery.presentation.request.DeliveryUpdateRequestDto;
import com.logistic.client.delivery.presentation.request.routeUpdateRequestDto;
import feign.Feign;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@Tag(name = "Delivery API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryApplicationService deliveryApplicationService;

    @Operation(
        summary = "배송 생성 (Feign)",
        description = "주문이 생성됨에 따라 배송, 배송 경로를 생성합니다."
    )
    @PostMapping("")
    public FeignDeliveryResponse createDelivery(@RequestBody CreateDeliveryRequest requestDto) {

        return deliveryApplicationService.createDelivery(requestDto);
    }

    @Operation(
        summary = "배송 단일 조회",
        description = "배송 ID로 특정 배송의 상세 정보를 조회합니다."
    )
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponseDto> readDelivery(@PathVariable("deliveryId") UUID deliveryId,
                                                            HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        String role = request.getHeader("role");

        DeliveryResponseDto responseDto = deliveryApplicationService.readDelivery(deliveryId, userId, role);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "배송 검색",
        description = "배송 매니저 ID를 기준으로 배송을 검색합니다."
    )
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

    @Operation(
        summary = "배송 수정",
        description = "상세한 배송 정보를 수정합니다."
    )
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

    @Operation(
        summary = "배송 상태 업데이트",
        description = "배송의 상태를 업데이트합니다."
    )
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

    @Operation(
        summary = "배송 경로 상태 업데이트",
        description = "배송 경로의 상태를 업데이트합니다."
    )
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

    @Operation(
        summary = "배송 경로 실제 소요시간, 거리 업데이트",
        description = "배송 경로에 걸린 실제 소요시간과 거리를 업데이트합니다."
    )
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

    @Operation(
        summary = "배송 삭제 (Feign)",
        description = "주문이 삭제됨에 따라 배송을 논리적 삭제합니다."
    )
    @PatchMapping("/{deliveryId}")
    public ResponseEntity<String> deleteDelivery(@PathVariable("deliveryId") UUID deliveryId,
                                                 HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);

        deliveryApplicationService.deleteDelivery(deliveryId, userId);
        return ResponseEntity.ok("삭제 성공 : " + deliveryId);
    }

}
