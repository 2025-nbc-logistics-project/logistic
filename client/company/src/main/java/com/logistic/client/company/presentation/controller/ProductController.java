package com.logistic.client.company.presentation.controller;

import com.logistic.client.company.application.dto.product.*;
import com.logistic.client.company.application.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    //상품 단일 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable UUID productId) {
        ProductResponseDto responseDto = productService.getProduct(productId);
        return ResponseEntity.ok(responseDto);
    }

}
