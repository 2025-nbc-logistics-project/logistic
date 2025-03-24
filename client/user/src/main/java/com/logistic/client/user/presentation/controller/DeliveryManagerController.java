package com.logistic.client.user.presentation.controller;

import com.logistic.client.user.presentation.requestDto.DeliveryManagerDTO;
import com.logistic.client.user.presentation.requestDto.UpdateDeliveryManagerDTO;
import com.logistic.client.user.application.service.DeliveryManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name ="DeliveryManager API", description = "배송담당자 관련 API")
@RestController
@RequestMapping("/api/v1/delivery-managers")
@RequiredArgsConstructor
public class DeliveryManagerController {
    private final DeliveryManagerService deliveryManagerService;

    @Operation(summary = "배송 담당자 등록", description = "배송 담당자 등록 처리")
    @PostMapping("")
    public ResponseEntity<?> createDeliveryManager(@Valid @RequestBody DeliveryManagerDTO requestDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ValidationErrorResponse(bindingResult));
        }

        String userRole = request.getHeader("role");
        String signInUsername = request.getHeader("username");
        return ResponseEntity.status(HttpStatus.OK).body(deliveryManagerService.createDeliveryManager(requestDto, userRole, signInUsername));
    }

    @Operation(summary = "배송 담당자 조회", description = "파라미터로 들어온 배송 담당자 조회")
    @GetMapping("/{deliveryManagerId}")
    public ResponseEntity<?> getDeliveryManager(@PathVariable String deliveryManagerId, HttpServletRequest request) {
        String signInUsername = request.getHeader("username");
        String userRole = request.getHeader("role");
        return ResponseEntity.status(HttpStatus.OK).body(deliveryManagerService.getDeliveryManager(deliveryManagerId, userRole, signInUsername));
    }

    @Operation(summary = "업체 배송 담당자 검색", description = "허브 아이디를 통해 해당 허브의 업체 배송 담당자 조회")
    @GetMapping("/hub/{hubId}")
    public ResponseEntity<?> getDeliveryManagersByHubId(@PathVariable UUID hubId, HttpServletRequest request) {
        String signInUsername = request.getHeader("username");
        String userRole = request.getHeader("role");
        return ResponseEntity.status(HttpStatus.OK).body(deliveryManagerService.getDeliveryManagersByHubId(hubId, userRole, signInUsername));
    }

    @Operation(summary = "배송 담당자 수정", description = "파라미터를 통해 들어온 배송 담당자 정보 수정")
    @PatchMapping("/update/{deliveryManagerId}")
    public ResponseEntity<?> updateDeliveryManager(@PathVariable String deliveryManagerId, @Valid @RequestBody UpdateDeliveryManagerDTO requestDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ValidationErrorResponse(bindingResult));
        }

        String signInUsername = request.getHeader("username");
        String userRole = request.getHeader("role");
        return ResponseEntity.status(HttpStatus.OK).body(deliveryManagerService.updateDeliveryManagers(deliveryManagerId, requestDto, userRole, signInUsername));
    }

    @Operation(summary = "배송 담당자 삭제", description = "파라미터를 통해 들어온 배송 담당자 논리적 삭제")
    @PatchMapping("/delete/{deliveryManagerId}")
    public ResponseEntity<?> deleteDeliveryManager(@PathVariable String deliveryManagerId, HttpServletRequest request) {
        String signInUsername = request.getHeader("username");
        String userRole = request.getHeader("role");
        deliveryManagerService.deleteDeliveryManager(deliveryManagerId, userRole, signInUsername);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Map<String, Object> ValidationErrorResponse(BindingResult bindingResult) {
        List<Map<String, String>> errors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage(),
                        "rejectedValue", String.valueOf(fieldError.getRejectedValue()) // 입력된 값도 포함
                ))
                .toList();

        return Map.of(
                "status", 400,
                "error", "Validation Field",
                "message", errors
        );
    }
}
