package com.logistic.client.company.presentation.controller;

import com.logistic.client.company.application.dto.product.*;
import com.logistic.client.company.application.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    //상품 등록
    @PostMapping("/products")
    public ResponseEntity<ProductCreateResponseDto> createProduct(@RequestBody @Valid ProductCreateRequestDto requestDto) {
        ProductCreateResponseDto responseDto = productService.createProduct(requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
