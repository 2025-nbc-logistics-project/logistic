package com.logistic.client.ai.presentation.controller;

import com.logistic.client.ai.application.dto.AiListResponseDto;
import com.logistic.client.ai.presentation.common.CommonResponse;
import com.logistic.client.ai.presentation.request.AiRequestDto;
import com.logistic.client.ai.application.dto.AiResponseDto;
import com.logistic.client.ai.application.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiController {
    
    private final AiService aiService;

    @PostMapping
    public ResponseEntity<CommonResponse<AiResponseDto>> createAi(
            @RequestBody AiRequestDto requestDto,
            @RequestHeader("userId") UUID userId
    ) {
        AiResponseDto aiResponseDto = aiService.createAi(requestDto);
        return ResponseEntity.ok(new CommonResponse<>(aiResponseDto, HttpStatus.OK.value(), "AI 생성 성공"));
    }

    //권한
    @GetMapping
    public ResponseEntity<CommonResponse<List<AiListResponseDto>>> getAis(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestHeader("role") String role
    ) {
        Page<AiListResponseDto> responseDtos = aiService.getAis(page-1, limit, sortBy, order, role);
        return ResponseEntity.ok(new CommonResponse<>(responseDtos.getContent(), HttpStatus.OK.value(), "AI 목록 조회 성공"));
    }

}
