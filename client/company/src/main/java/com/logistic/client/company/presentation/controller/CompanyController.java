package com.logistic.client.company.presentation.controller;

import com.logistic.client.company.application.dto.company.*;
import com.logistic.client.company.application.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    //로그인, 권한 확인

    //업체 등록
    @PostMapping("/companies")
    public ResponseEntity<CompanyCreateResponseDto> createCompany(@RequestBody @Valid CompanyCreateRequestDto requestDto) {
        CompanyCreateResponseDto responseDto = companyService.createCompany(requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
