package com.logistic.client.company.application.service;

import com.logistic.client.company.application.dto.product.*;
import com.logistic.client.company.domain.exception.product.ProductNotFoundException;
import com.logistic.client.company.domain.model.product.Product;
import com.logistic.client.company.domain.model.product.ProductInfo;
import com.logistic.client.company.domain.model.product.Quantity;
import com.logistic.client.company.domain.repository.ProductRepository;
import com.logistic.client.company.infrastructure.client.HubClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;
    //
    private final CompanyService companyService;
    private final HubClient hubClient;

    @Transactional
    public ProductCreateResponseDto createProduct(ProductCreateRequestDto requestDto) {

        //중복 검사 (??)

        //존재하는 업체인지
        companyService.findByCompanyId(requestDto.getCompanyId());

        //존재하는 허브인지
//        if(!hubClient.existsHub(requestDto.getHudId())) {
//            throw new HubNotFoundException();
//        }

        Product product = new Product(requestDto);
        productRepository.save(product);
        return new ProductCreateResponseDto(product);

    }

    public ProductResponseDto getProduct(UUID productId) {

        Product product = findByProductId(productId);

        return new ProductResponseDto(product);
    }

    public Page<ProductListResponseDto> getProducts(int page, int limit, String sortBy, String order) {

        if(limit != 10 && limit != 30 && limit != 50) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Product> products = productRepository.getProducts(pageable, sortBy, order);

        return products.map(ProductListResponseDto::new);
    }

    public Page<ProductListResponseDto> getSearchProducts(String key, UUID company, UUID hub, int page, int limit, String sortBy, String order) {

        if(limit != 10 && limit != 30 && limit != 50) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Product> products = productRepository.getSearchProducts(key, company, hub, pageable, sortBy, order);

        return products.map(ProductListResponseDto::new);
    }

    @Transactional
    public List<ProductPriceResponseDto> checkAndDeductStock(List<OrderItemRequestDto> orderItems) {

        List<ProductPriceResponseDto> responseDtoList = new ArrayList<>();

        for(OrderItemRequestDto orderItem : orderItems) {
            log.debug("주문 상품: {}, 주문 개수: {}", orderItem.getProductId(), orderItem.getQuantity());

            Product product = entityManager.find(Product.class, orderItem.getProductId(), LockModeType.PESSIMISTIC_WRITE);

            if(product == null) {
                throw new ProductNotFoundException();
            }

            log.debug("원래 상품 개수: {}", product.getQuantity().getQuantity());

            //재고 확인 후 차감
            product.deductStock(orderItem.getQuantity());

            entityManager.flush();

            log.debug("차감 후 개수: {}", product.getQuantity().getQuantity());

            //상품 id와 가격 반환
            responseDtoList.add(
                    new ProductPriceResponseDto(
                            orderItem.getProductId(),
                            product.getProductInfo().getPrice() * orderItem.getQuantity()));
        }

        return responseDtoList;

    }

    @Transactional
    public void restoreStock(List<OrderItemRequestDto> restoreList) {

        for(OrderItemRequestDto restore : restoreList) {
            log.debug("주문 상품: {}, 재입고 개수: {}", restore.getProductId(), restore.getQuantity());

            Product product = entityManager.find(Product.class, restore.getProductId(), LockModeType.PESSIMISTIC_WRITE);

            if(product == null) {
                throw new ProductNotFoundException();
            }

            log.debug("원래 상품 개수: {}", product.getQuantity().getQuantity());

            product.restoreStock(restore.getQuantity());

            entityManager.flush();

            log.debug("재입고 후 개수: {}", product.getQuantity().getQuantity());

        }
    }

    @Transactional
    public ProductUpdateResponseDto updateProduct(UUID productId, ProductUpdateRequestDto requestDto) {

        //존재하는 상품인지
        Product product = findByProductId(productId);

        //허브

        String productName = product.getProductInfo().getProductName();
        Integer price = product.getProductInfo().getPrice();

        if(requestDto.getProductName() != null && !requestDto.getProductName().isBlank()) {
            productName = requestDto.getProductName();
        }

        if(requestDto.getPrice() != null) {
            price = requestDto.getPrice();
        }

        ProductInfo newProductInfo = new ProductInfo(productName, price);

        if(!product.getProductInfo().equals(newProductInfo)) {
            product.changeProductInfo(newProductInfo);
        }

        if(requestDto.getQuantity() != null && !product.getQuantity().equals(new Quantity(requestDto.getQuantity()))) {
            product.changeQuantity(requestDto.getQuantity());
        }

        return new ProductUpdateResponseDto(product);
    }

    @Transactional
    public ProductDeleteResponseDto deleteProduct(UUID productId) {
        Product product = findByProductId(productId);
        //
        product.delete(1L);

        return new ProductDeleteResponseDto(product);
    }

    public Product findByProductId(UUID productId) {
        return productRepository
                .findByProductIdAndDeletedAtIsNull(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

}
