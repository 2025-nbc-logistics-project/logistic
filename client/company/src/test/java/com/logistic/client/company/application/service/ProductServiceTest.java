package com.logistic.client.company.application.service;

import static org.mockito.BDDMockito.*;

import com.logistic.client.company.application.dto.common.CompanyExistResponseDto;
import com.logistic.client.company.application.dto.common.HubDto;
import com.logistic.client.company.application.dto.common.UserDto;
import com.logistic.client.company.infrastructure.client.HubClient;
import com.logistic.client.company.infrastructure.client.UserClient;
import com.logistic.client.company.presentation.request.CompanyCreateRequestDto;
import com.logistic.client.company.application.dto.product.*;
import com.logistic.client.company.domain.model.company.Company;
import com.logistic.client.company.domain.model.company.CompanyType;
import com.logistic.client.company.domain.model.product.Product;
import com.logistic.client.company.domain.repository.ProductRepository;
import com.logistic.client.company.presentation.request.OrderItemRequestDto;
import com.logistic.client.company.presentation.request.ProductCreateRequestDto;
import com.logistic.client.company.presentation.request.ProductUpdateRequestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private HubClient hubClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private UUID productId;
    private Company company;
    private UUID userId = UUID.randomUUID();
    private String role = "HUB_MANAGER";
    private String username = "test";

    @BeforeEach
    void setUp() {

        UUID hubId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CompanyType companyType = CompanyType.producer;
        String companyName = "업체1";
        String companyTel = "07012345678";
        String postalCode = "01234";
        String streetAddress = "서울시 중구";
        String detailAddress = "1층";
        UUID companyId = UUID.randomUUID();
        String productName = "연필";
        int price = 500;
        int quantity = 10;

        CompanyCreateRequestDto requestDto = new CompanyCreateRequestDto(
                hubId, companyType, companyName, companyTel, postalCode, streetAddress, detailAddress);

        company = new Company(requestDto, userId);
        product = new Product(new ProductCreateRequestDto(companyId, hubId, productName, price, quantity));
        productId = UUID.randomUUID();
        ReflectionTestUtils.setField(product, "productId", productId);
    }

    @Test
    @DisplayName("상품 생성 성공")
    void createProductSuccess() {

        ProductCreateRequestDto requestDto = new ProductCreateRequestDto(
                product.getCompanyId(),
                product.getHubId(),
                product.getProductInfo().getProductName(),
                product.getProductInfo().getPrice(),
                product.getQuantity().getQuantity()
        );
        HubDto hubDto = new HubDto();
        ReflectionTestUtils.setField(hubDto, "id", product.getHubId());
        ReflectionTestUtils.setField(hubDto, "name", "허브1");

        UserDto userDto = new UserDto();
        ReflectionTestUtils.setField(userDto, "hubId", company.getHubId());

        given(companyService.getCompanyById(requestDto.getCompanyId())).willReturn(new CompanyExistResponseDto(company));
        given(hubClient.getHub(requestDto.getHubId())).willReturn(hubDto);
        given(userClient.getHubId(username)).willReturn(userDto);
        doNothing().when(productRepository).save(any(Product.class));

        ProductCreateResponseDto responseDto = productService.createProduct(requestDto, userId, username, role);

        assertNotNull(responseDto);
        assertEquals("연필", responseDto.getProductName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 단일 조회 성공")
    void getProductSuccess() {

        given(productRepository.findByProductIdAndDeletedAtIsNull(productId)).willReturn(Optional.of(product));

        ProductResponseDto responseDto = productService.getProduct(productId);

        assertNotNull(responseDto);
        assertEquals("연필", responseDto.getProductName());
    }

    @Test
    @DisplayName("상품 목록 조회 성공")
    void getProductsSuccess() {

        int page = 0;
        int limit = 10;
        String sortBy = "updatedBy";
        String order = "asc";
        Pageable pageable = PageRequest.of(page, limit);

        List<Product> productList = List.of(product);
        Page<Product> productPage = new PageImpl<>(productList);

        given(productRepository.getProducts(pageable, sortBy, order)).willReturn(productPage);

        Page<ProductListResponseDto> responseDtos = productService.getProducts(page, limit, sortBy, order);

        assertEquals(0, responseDtos.getNumber());
        assertEquals("연필", responseDtos.getContent().get(0).getProductName());
    }

    @Test
    @DisplayName("상품 검색 조회 성공")
    void getSearchProductsSuccess() {

        String key = "연필";
        UUID company = UUID.randomUUID();
        UUID hub = UUID.randomUUID();
        int page = 0;
        int limit = 10;
        String sortBy = "updatedBy";
        String order = "asc";
        Pageable pageable = PageRequest.of(page, limit);

        List<Product> productList = List.of(product);
        Page<Product> productPage = new PageImpl<>(productList);

        given(productRepository.getSearchProducts(key, company, hub, pageable, sortBy, order)).willReturn(productPage);

        Page<ProductListResponseDto> responseDtos = productService.getSearchProducts(key,company, hub, page, limit, sortBy, order);

        assertNotNull(responseDtos);
        assertEquals("연필", responseDtos.getContent().get(0).getProductName());
    }

    @Test
    @DisplayName("주문시 상품 수량 변경 성공")
    void checkAndDeductStockSuccess() {

        OrderItemRequestDto requestDto = new OrderItemRequestDto(productId, 3);
        List<OrderItemRequestDto> orderItems = List.of(requestDto);

        EntityManager entityManager = mock(EntityManager.class);
        ReflectionTestUtils.setField(productService, "entityManager", entityManager);

        given(entityManager.find(Product.class, requestDto.getProductId(), LockModeType.PESSIMISTIC_WRITE)).willReturn(product);

        List<ProductPriceResponseDto> responseDtos = productService.checkAndDeductStock(orderItems);

        assertNotNull(responseDtos);
        assertEquals(7, product.getQuantity().getQuantity());
        assertEquals(1500, responseDtos.get(0).getPrice());
    }

    @Test
    @DisplayName("주문 취소시 상품 수량 변경 성공")
    void restoreStockSuccess() {

        OrderItemRequestDto requestDto = new OrderItemRequestDto(productId, 3);
        List<OrderItemRequestDto> restoreItems = List.of(requestDto);

        EntityManager entityManager = mock(EntityManager.class);
        ReflectionTestUtils.setField(productService, "entityManager", entityManager);

        given(entityManager.find(Product.class, requestDto.getProductId(), LockModeType.PESSIMISTIC_WRITE)).willReturn(product);

        productService.restoreStock(restoreItems);

        assertEquals(13, product.getQuantity().getQuantity());
    }

    @Test
    @DisplayName("삼품 수정 성공")
    void updateProductSuccess() {

        given(productRepository.findByProductIdAndDeletedAtIsNull(productId)).willReturn(Optional.ofNullable(product));

        ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto(
                UUID.randomUUID(),
                "볼펜",
                1000,
                15
        );

        UserDto userDto = new UserDto();
        ReflectionTestUtils.setField(userDto, "hubId", company.getHubId());

        given(userClient.getHubId(username)).willReturn(userDto);

        ProductUpdateResponseDto responseDto = productService.updateProduct(productId, requestDto, userId, username, role);

        assertNotNull(responseDto);
        assertEquals("볼펜", responseDto.getProductName());
        assertEquals(1000, responseDto.getPrice());
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProductSuccess() {

        UserDto userDto = new UserDto();
        ReflectionTestUtils.setField(userDto, "hubId", company.getHubId());

        given(userClient.getHubId(username)).willReturn(userDto);
        given(productRepository.findByProductIdAndDeletedAtIsNull(productId)).willReturn(Optional.ofNullable(product));

        ProductDeleteResponseDto responseDto = productService.deleteProduct(productId, userId, username, role);

        assertNotNull(responseDto);
        assertEquals(userId, responseDto.getDeletedBy());
    }

    @Test
    @DisplayName("productId로 상품 조회 성공")
    void findByProductIdSuccess() {

        given(productRepository.findByProductIdAndDeletedAtIsNull(productId)).willReturn(Optional.of(product));

        Product product1 = productService.findByProductId(productId);

        assertNotNull(product1);
        assertEquals(productId, product1.getProductId());
    }
}