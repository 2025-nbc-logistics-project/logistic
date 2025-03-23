package com.logistic.client.user.infrastructure.client;

import com.logistic.client.user.application.dto.responseDto.CompanyResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company")
public interface CompanyClient {
    @GetMapping("/api/companies/{companyId}")
    CompanyResDTO getCompany(@PathVariable("companyId") UUID companyId);
}
