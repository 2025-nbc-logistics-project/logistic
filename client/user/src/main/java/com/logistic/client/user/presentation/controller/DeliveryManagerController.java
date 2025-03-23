package com.logistic.client.user.presentation.controller;

import com.logistic.client.user.application.dto.requestDto.DeliveryManagerDTO;
import com.logistic.client.user.application.dto.requestDto.UpdateDeliveryManagerDTO;
import com.logistic.client.user.application.service.DeliveryManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/delivery-managers")
@RequiredArgsConstructor
public class DeliveryManagerController {
    private final DeliveryManagerService deliveryManagerService;

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

    @GetMapping("/{deliveryManagerId}")
    public ResponseEntity<?> getDeliveryManager(@PathVariable String deliveryManagerId, HttpServletRequest request) {
        String signInUsername = request.getHeader("username");
        String userRole = request.getHeader("role");
        return ResponseEntity.status(HttpStatus.OK).body(deliveryManagerService.getDeliveryManager(deliveryManagerId, userRole, signInUsername));
    }

    //검색 및 페이징 기능 추후 추가 예정
//    @GetMapping("/")
//    public ResponseEntity<?> getDeliveryManagers(@RequestParam, HttpServletRequest request) {
//        String signInUsername = request.getHeader("username");
//        String userRole = request.getHeader("role");
//        return ResponseEntity.status(HttpStatus.OK).body(deliveryManagerService.getDeliveryManager(deliveryManagerId, userRole, signInUsername));
//    }

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
