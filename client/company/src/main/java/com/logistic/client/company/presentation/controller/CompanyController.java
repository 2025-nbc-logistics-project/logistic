package com.logistic.client.company.presentation.controller;

import com.logistic.client.company.application.dto.company.*;
import com.logistic.client.company.application.service.CompanyService;
import com.logistic.client.company.presentation.common.CommonResponse;
import com.logistic.client.company.presentation.request.CompanyCreateRequestDto;
import com.logistic.client.company.presentation.request.CompanyUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(
            summary = "업체 등록",
            description = "새로운 업체를 등록합니다.",
            security = {
                    @SecurityRequirement(name = "userId"),
                    @SecurityRequirement(name = "username"),
                    @SecurityRequirement(name = "role")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "업체 등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력 값 검증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "데이터 없음"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 업체")
    })
    @PostMapping
    public ResponseEntity<CommonResponse<CompanyCreateResponseDto>> createCompany(
            @Parameter(description = "등록할 업체 데이터")
            @RequestBody @Valid CompanyCreateRequestDto requestDto,
            @Parameter(description = "현재 사용자 userId")
            @RequestHeader("userId") UUID userId,
            @Parameter(description = "현재 사용자 username")
            @RequestHeader("username") String username,
            @Parameter(description = "등록할 사용자 role")
            @RequestHeader("role") String role
    ) {
        CompanyCreateResponseDto responseDto = companyService.createCompany(requestDto, userId, username, role);
        return ResponseEntity.ok(new CommonResponse<>(responseDto, HttpStatus.OK.value(), "업체 등록 성공"));
    }

    @Operation(summary = "업체 단일 조회", description = "특정 업체를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 단일 조회 성공"),
            @ApiResponse(responseCode = "404", description = "업체를 찾을 수 없음")
    })
    @GetMapping("/{companyId}")
    public ResponseEntity<CommonResponse<CompanyResponseDto>> getCompany(
            @Parameter(description = "특정 업체 UUID")
            @PathVariable UUID companyId
    ) {
        CompanyResponseDto responseDto = companyService.getCompany(companyId);
        return ResponseEntity.ok(new CommonResponse<>(responseDto, HttpStatus.OK.value(), "특정 업체 조회 성공"));
    }

    @Operation(summary = "업체 목록 조회", description = "업체 전체를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public ResponseEntity<CommonResponse<List<CompanyListResponseDto>>> getCompanies(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @Parameter(description = "보여줄 업체 개수")
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @Parameter(description = "정렬 기준 필드 (createdAt, updatedAt)")
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방식 (asc: 오름차순, desc: 내림차순)")
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ) {
        Page<CompanyListResponseDto> responseDtos = companyService.getCompanies(page-1, limit, sortBy, order);
        return ResponseEntity.ok(new CommonResponse<>(responseDtos.getContent(), HttpStatus.OK.value(), "업체 전체 조회 성공"));
    }

    @Operation(summary = "업체 검색 조회", description = "업체 이름을 포함하는 검색 결과를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 검색 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<CompanyListResponseDto>>> getSearchCompanies(
            @Parameter(description = "업체 이름 조회 키워드")
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @Parameter(description = "보여줄 업체 개수")
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @Parameter(description = "정렬 기준 필드 (createdAt, updatedAt)")
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방식 (asc: 오름차순, desc: 내림차순)")
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ) {
        Page<CompanyListResponseDto> responseDtos = companyService.getSearchCompanies(key, page-1, limit, sortBy, order);
        return ResponseEntity.ok(new CommonResponse<>(responseDtos.getContent(), HttpStatus.OK.value(), "업체 검색 조회 성공"));
    }

    @Operation(
            summary = "업체 수정",
            description = "업체를 수정합니다.",
            security = {
                    @SecurityRequirement(name = "userId"),
                    @SecurityRequirement(name = "username"),
                    @SecurityRequirement(name = "role")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "데이터 없음")
    })
    @PatchMapping("/{companyId}")
    public ResponseEntity<CommonResponse<CompanyUpdateResponseDto>> updateCompany(
            @Parameter(description = "특정 업체 UUID")
            @PathVariable UUID companyId,
            @Parameter(description = "수정할 업체 데이터")
            @RequestBody CompanyUpdateRequestDto requestDto,
            @Parameter(description = "현재 사용자 userId")
            @RequestHeader("userId") UUID userId,
            @Parameter(description = "현재 사용자 username")
            @RequestHeader("username") String username,
            @Parameter(description = "등록할 사용자 role")
            @RequestHeader("role") String role
    ) {
        CompanyUpdateResponseDto responseDto = companyService.updateCompany(companyId, requestDto, userId, username, role);
        return ResponseEntity.ok(new CommonResponse<>(responseDto, HttpStatus.OK.value(), "업체 수정 성공"));
    }

    @Operation(
            summary = "업체 삭제",
            description = "업체를 삭제합니다.",
            security = {
                    @SecurityRequirement(name = "userId"),
                    @SecurityRequirement(name = "username"),
                    @SecurityRequirement(name = "role")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "데이터 없음"),
            @ApiResponse(responseCode = "404", description = "이미 삭제됨")
    })
    @PatchMapping("/{companyId}/delete")
    public ResponseEntity<CommonResponse<CompanyDeleteResponseDto>> deleteCompany(
            @Parameter(description = "특정 업체 UUID")
            @PathVariable UUID companyId,
            @Parameter(description = "현재 사용자 userId")
            @RequestHeader("userId") UUID userId,
            @Parameter(description = "현재 사용자 username")
            @RequestHeader("username") String username,
            @Parameter(description = "등록할 사용자 role")
            @RequestHeader("role") String role
    ) {
        CompanyDeleteResponseDto responseDto = companyService.deleteCompany(companyId, userId, username, role);
        return ResponseEntity.ok(new CommonResponse<>(responseDto, HttpStatus.OK.value(), "업체 삭제 성공"));
    }

}
