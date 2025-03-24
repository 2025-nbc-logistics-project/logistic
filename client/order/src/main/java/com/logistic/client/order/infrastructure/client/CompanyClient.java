package com.logistic.client.order.infrastructure.client;

import com.logistic.client.order.application.dto.CompanyResponse;
import com.logistic.client.order.presentation.request.OrderItemRequestDto;
import com.logistic.client.order.application.dto.ProductPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "company")
public interface CompanyClient {
    @PostMapping("/api/v1/products/deduct-stock")
    List<ProductPriceResponse> checkAndDeductStock(@RequestBody List<OrderItemRequestDto> orderItems);

    @GetMapping("/api/v1/companies/feign/{companyId}")
    CompanyResponse getCompany(@PathVariable("companyId") UUID companyId);

    @PostMapping("/api/v1/products/restore-stock")
    void restoreStock(@RequestBody List<OrderItemRequestDto> restoreList);
}
