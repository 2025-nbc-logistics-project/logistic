package com.logistic.client.company.presentation.controller;

import com.logistic.client.company.application.dto.product.*;
import com.logistic.client.company.application.service.ProductService;
import com.logistic.client.company.presentation.common.CommonResponse;
import com.logistic.client.company.presentation.request.OrderItemRequestDto;
import com.logistic.client.company.presentation.request.ProductCreateRequestDto;
import com.logistic.client.company.presentation.request.ProductUpdateRequestDto;
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
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "상품 등록",
            description = "새로운 상품 등록합니다.",
            security = {
                    @SecurityRequirement(name = "userId"),
                    @SecurityRequirement(name = "username"),
                    @SecurityRequirement(name = "role")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력 값 검증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "데이터 없음")
    })
    @PostMapping
    public ResponseEntity<CommonResponse<ProductCreateResponseDto>> createProduct(
            @Parameter(description = "등록할 상품 데이터")
            @RequestBody @Valid ProductCreateRequestDto requestDto,
            @Parameter(description = "현재 사용자 userId")
            @RequestHeader("userId") UUID userId,
            @Parameter(description = "현재 사용자 username")
            @RequestHeader("username") String username,
            @Parameter(description = "등록할 사용자 role")
            @RequestHeader("role") String role
    ) {
        ProductCreateResponseDto responseDto = productService.createProduct(requestDto, userId, username, role);
        return ResponseEntity.ok(new CommonResponse<>(responseDto, HttpStatus.OK.value(), "상품 등록 성공"));
    }

    @Operation(summary = "상품 단일 조회", description = "특정 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 단일 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품를 찾을 수 없음")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponseDto>> getProduct(
            @Parameter(description = "특정 상품 UUID")
            @PathVariable UUID productId
    ) {
        ProductResponseDto responseDto = productService.getProduct(productId);
        return ResponseEntity.ok(new CommonResponse<>(responseDto, HttpStatus.OK.value(), "특정 상품 조회 성공"));
    }

    @Operation(summary = "상품 목록 조회", description = "상품 전체를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public ResponseEntity<CommonResponse<List<ProductListResponseDto>>> getProducts(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @Parameter(description = "보여줄 업체 개수")
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @Parameter(description = "정렬 기준 필드 (createdAt, updatedAt)")
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방식 (asc: 오름차순, desc: 내림차순)")
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ) {
        Page<ProductListResponseDto> responseDtos = productService.getProducts(page-1, limit, sortBy, order);
        return ResponseEntity.ok(new CommonResponse<>(responseDtos.getContent(), HttpStatus.OK.value(), "상품 전체 조회 성공"));
    }

    //상품 검색 조회 (상품 이름, 업체, 허브)
    @Operation(summary = "상품 검색 조회", description = "상품 이름, 업체, 허브를 검색 결과를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 검색 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ProductListResponseDto>>> getSearchProducts(
            @Parameter(description = "상품 이름 조회 키워드")
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @Parameter(description = "업체 조회 키워드")
            @RequestParam(value = "company", required = false, defaultValue = "") UUID company,
            @Parameter(description = "허브 조회 키워드")
            @RequestParam(value = "hub", required = false, defaultValue = "") UUID hub,
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @Parameter(description = "보여줄 업체 개수")
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @Parameter(description = "정렬 기준 필드 (createdAt, updatedAt)")
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방식 (asc: 오름차순, desc: 내림차순)")
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ){
        Page<ProductListResponseDto> responseDtos = productService.getSearchProducts(key, company, hub, page-1, limit, sortBy, order);
        return ResponseEntity.ok(new CommonResponse<>(responseDtos.getContent(), HttpStatus.OK.value(), "상품 검색 조회 성공"));
    }

    //주문시 상품 수량 변경
    @Operation(
            summary = "주문시 상품 수량 변경",
            description = "주문 시 상품 개수를 차감합니다.",
            security = {
                    @SecurityRequirement(name = "userId"),
                    @SecurityRequirement(name = "role")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 수량 변경 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "데이터 없음")
    })
    @PostMapping("/deduct-stock")
    public List<ProductPriceResponseDto> checkAndDeductStock(
            @Parameter(description = "주문 상품 리스트")
            @RequestBody List<OrderItemRequestDto> orderItems
    ) {
        return productService.checkAndDeductStock(orderItems);
    }

    //주문 취소시 상품 수량 변경
    @Operation(
            summary = "주문 취소 시 상품 수량 변경",
            description = "주문 취소 시 상품 개수를 더합니다.",
            security = {
                    @SecurityRequirement(name = "userId"),
                    @SecurityRequirement(name = "role")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 수량 변경 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "데이터 없음")
    })
    @PostMapping("/restore-stock")
    public void restoreStock(
            @Parameter(description = "주문 취소 상품 리스트")
            @RequestBody List<OrderItemRequestDto> restoreList
    ) {
        productService.restoreStock(restoreList);
    }

    @Operation(
            summary = "상품 수정",
            description = "상품을 수정합니다.",
            security = {
                    @SecurityRequirement(name = "userId"),
                    @SecurityRequirement(name = "username"),
                    @SecurityRequirement(name = "role")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "데이터 없음")
    })
    @PatchMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductUpdateResponseDto>> updateProduct(
            @Parameter(description = "특정 상품 UUID")
            @PathVariable UUID productId,
            @Parameter(description = "수정할 상품 데이터")
            @RequestBody ProductUpdateRequestDto requestDto,
            @Parameter(description = "현재 사용자 userId")
            @RequestHeader("userId") UUID userId,
            @Parameter(description = "현재 사용자 username")
            @RequestHeader("username") String username,
            @Parameter(description = "등록할 사용자 role")
            @RequestHeader("role") String role
    ) {
        ProductUpdateResponseDto responseDto = productService.updateProduct(productId, requestDto, userId, username, role);
        return ResponseEntity.ok(new CommonResponse<>(responseDto, HttpStatus.OK.value(), "상품 수정 성공"));
    }

    @Operation(
            summary = "상품 삭제",
            description = "상품을 삭제합니다.",
            security = {
                    @SecurityRequirement(name = "userId"),
                    @SecurityRequirement(name = "username"),
                    @SecurityRequirement(name = "role")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "데이터 없음"),
            @ApiResponse(responseCode = "404", description = "이미 삭제됨")
    })
    @PatchMapping("/{productId}/delete")
    public ResponseEntity<CommonResponse<ProductDeleteResponseDto>> deleteProduct(
            @Parameter(description = "특정 상품 UUID")
            @PathVariable UUID productId,
            @Parameter(description = "현재 사용자 userId")
            @RequestHeader("userId") UUID userId,
            @Parameter(description = "현재 사용자 username")
            @RequestHeader("username") String username,
            @Parameter(description = "등록할 사용자 role")
            @RequestHeader("role") String role
    ) {
        ProductDeleteResponseDto responseDto = productService.deleteProduct(productId, userId, username, role);
        return ResponseEntity.ok(new CommonResponse<>(responseDto, HttpStatus.OK.value(), "상품 삭제 성공"));
    }

}
