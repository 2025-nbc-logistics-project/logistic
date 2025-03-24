package com.logistic.client.ai.presentation.controller;

import com.logistic.client.ai.application.dto.AiListResponseDto;
import com.logistic.client.ai.presentation.common.CommonResponse;
import com.logistic.client.ai.presentation.request.AiRequestDto;
import com.logistic.client.ai.application.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiController {
    
    private final AiService aiService;

    @Operation(
            summary = "AI 메시지 생성",
            description = "새로운 AI 메시지를 생성합니다.",
            security = {
                    @SecurityRequirement(name = "userId")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "AI 메시지 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 오류")
    })
    @PostMapping
    public String createAi(
            @Parameter(description = "AI 메시지 생성을 위한 데이터")
            @RequestBody AiRequestDto requestDto
    ) {
        return aiService.createAi(requestDto);
    }

    //권한
    @Operation(
            summary = "AI 메시지 목록 조회",
            description = "AI 메시지 전체를 조회합니다.",
            security = {
                    @SecurityRequirement(name = "role")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "AI 메시지 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 오류")
    })
    @GetMapping
    public ResponseEntity<CommonResponse<List<AiListResponseDto>>> getAis(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @Parameter(description = "보여줄 메시지 개수")
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @Parameter(description = "정렬 기준 필드 (createdAt, updatedAt)")
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방식 (asc: 오름차순, desc: 내림차순)")
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @Parameter(description = "현재 사용자 role")
            @RequestHeader("role") String role
    ) {
        Page<AiListResponseDto> responseDtos = aiService.getAis(page-1, limit, sortBy, order, role);
        return ResponseEntity.ok(new CommonResponse<>(responseDtos.getContent(), HttpStatus.OK.value(), "AI 목록 조회 성공"));
    }

}
