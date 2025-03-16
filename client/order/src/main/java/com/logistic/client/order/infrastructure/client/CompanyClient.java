package com.logistic.client.order.infrastructure.client;

import com.logistic.client.order.application.dto.CompanyResponse;
import com.logistic.client.order.application.dto.OrderItemRequestDto;
import com.logistic.client.order.application.dto.ProductPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "company-service")
public interface CompanyClient {
    @PostMapping("/api/products/stock")
    List<ProductPriceResponse> checkAndDeductStock(@RequestBody List<OrderItemRequestDto> orderItems);

    @GetMapping("/api/companies/{companyId}")
    CompanyResponse getCompany(@PathVariable("companyId") UUID companyId);
}
