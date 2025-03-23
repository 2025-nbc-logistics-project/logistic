package com.logistic.client.company.presentation.controller;

import com.logistic.client.company.application.dto.company.*;
import com.logistic.client.company.application.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    //로그인, 권한 확인

    //업체 등록
    @PostMapping
    public ResponseEntity<CompanyCreateResponseDto> createCompany(@RequestBody @Valid CompanyCreateRequestDto requestDto) {
        CompanyCreateResponseDto responseDto = companyService.createCompany(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    //업체 단일 조회
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable UUID companyId) {
        CompanyResponseDto responseDto = companyService.getCompany(companyId);
        return ResponseEntity.ok(responseDto);
    }

    //업체 목록 조회
    //
    @GetMapping
    public Page<CompanyListResponseDto> getCompanies(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ) {
        return companyService.getCompanies(page-1, limit, sortBy, order);
    }

    //업체 검색 조회 (이름)
    @GetMapping("/search")
    public Page<CompanyListResponseDto> getSearchCompanies(
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ) {
        return companyService.getSearchCompanies(key, page-1, limit, sortBy, order);
    }

    //업체 수정
    @PatchMapping("/{companyId}")
    public ResponseEntity<CompanyUpdateResponseDto> updateCompany(
            @PathVariable UUID companyId,
            @RequestBody CompanyUpdateRequestDto requestDto
    ) {
        CompanyUpdateResponseDto responseDto = companyService.updateCompany(companyId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    //업체 삭제
    @PatchMapping("/{companyId}/delete")
    public ResponseEntity<CompanyDeleteResponseDto> deleteCompany(@PathVariable UUID companyId) {
        CompanyDeleteResponseDto responseDto = companyService.deleteCompany(companyId);
        return ResponseEntity.ok(responseDto);
    }

}
