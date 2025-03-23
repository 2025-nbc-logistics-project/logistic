package com.logistic.client.company.presentation.controller;

import com.logistic.client.company.application.dto.product.*;
import com.logistic.client.company.application.service.ProductService;
import com.logistic.client.company.presentation.request.OrderItemRequestDto;
import com.logistic.client.company.presentation.request.ProductCreateRequestDto;
import com.logistic.client.company.presentation.request.ProductUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    //상품 등록
    @PostMapping
    public ResponseEntity<ProductCreateResponseDto> createProduct(
            @RequestBody @Valid ProductCreateRequestDto requestDto,
            @RequestHeader("userId") UUID userId,
            @RequestHeader("hubId") UUID hubId,
            @RequestHeader("role") String role
    ) {
        ProductCreateResponseDto responseDto = productService.createProduct(requestDto, userId, hubId, role);
        return ResponseEntity.ok(responseDto);
    }

    //상품 단일 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable UUID productId) {
        ProductResponseDto responseDto = productService.getProduct(productId);
        return ResponseEntity.ok(responseDto);
    }

    //상품 전체 조회
    @GetMapping
    public Page<ProductListResponseDto> getProducts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ) {
        return productService.getProducts(page-1, limit, sortBy, order);
    }

    //상품 검색 조회 (상품 이름, 업체, 허브)
    //
    @GetMapping("/search")
    public Page<ProductListResponseDto> getSearchProducts(
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @RequestParam(value = "company", required = false, defaultValue = "") UUID company,
            @RequestParam(value = "hub", required = false, defaultValue = "") UUID hub,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ){
        return productService.getSearchProducts(key, company, hub, page-1, limit, sortBy, order);
    }

    //주문시 상품 수량 변경
    @PostMapping("/deduct-stock")
    public List<ProductPriceResponseDto> checkAndDeductStock(
            @RequestBody List<OrderItemRequestDto> orderItems,
            @RequestHeader("userId") UUID userId,
            @RequestHeader("role") String role
    ) {
        return productService.checkAndDeductStock(orderItems, role);
    }

    //주문 취소시 상품 수량 변경
    @PostMapping("/restore-stock")
    public void restoreStock(
            @RequestBody List<OrderItemRequestDto> restoreList,
            @RequestHeader("userId") UUID userId,
            @RequestHeader("role") String role
    ) {
        productService.restoreStock(restoreList, role);
    }

    //상품 수정
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponseDto> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductUpdateRequestDto requestDto,
            @RequestHeader("userId") UUID userId,
            @RequestHeader("hubId") UUID hubId,
            @RequestHeader("role") String role
    ) {
        ProductUpdateResponseDto responseDto = productService.updateProduct(productId, requestDto, userId, hubId, role);
        return ResponseEntity.ok(responseDto);
    }

    //상품 삭제
    @PatchMapping("/{productId}/delete")
    public ResponseEntity<ProductDeleteResponseDto> deleteProduct(
            @PathVariable UUID productId,
            @RequestHeader("userId") UUID userId,
            @RequestHeader("hubId") UUID hubId,
            @RequestHeader("role") String role
    ) {
        ProductDeleteResponseDto responseDto = productService.deleteProduct(productId, userId, hubId, role);
        return ResponseEntity.ok(responseDto);
    }

}
