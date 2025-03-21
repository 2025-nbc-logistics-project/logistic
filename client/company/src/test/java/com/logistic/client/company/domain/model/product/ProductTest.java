package com.logistic.client.company.domain.model.product;

import com.logistic.client.company.application.dto.product.ProductCreateRequestDto;
import com.logistic.client.company.domain.exception.product.ProductValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {

        UUID companyId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();
        String productName = "연필";
        int price = 500;
        int quantity = 10;

        product = new Product(new ProductCreateRequestDto(companyId, hubId, productName, price, quantity));
    }

    @Test
    @DisplayName("주문 시 수량 변경 성공")
    void deductStockSuccess() {

        Integer orderQuantity = 3;

        product.deductStock(orderQuantity);

        assertEquals(7, product.getQuantity().getQuantity());
    }

    @Test
    @DisplayName("주문 시 수량 변경 실패")
    void deductStockFailure() {
        assertThrows(ProductValidationException.class, () -> product.deductStock(100));
    }

    @Test
    @DisplayName("주문 취소 시 수량 변경 성공")
    void restoreStockSuccess() {

        Integer restoreQuantity = 3;

        product.restoreStock(restoreQuantity);

        assertEquals(13, product.getQuantity().getQuantity());
    }

    @Test
    @DisplayName("주문 취소 시 수량 변경 실패")
    void restoreStockFailure() {
        assertThrows(ProductValidationException.class, () -> product.restoreStock(-100));
    }

    @Test
    @DisplayName("제품 정보 수정 성공")
    void changeProductInfoSuccess() {

        ProductInfo productInfo = product.getProductInfo();

        ProductInfo newProductInfo = new ProductInfo("볼펜", 1000);

        product.changeProductInfo(newProductInfo);

        assertEquals("볼펜", product.getProductInfo().getProductName());
        assertEquals(1000,product.getProductInfo().getPrice());
        assertNotEquals(productInfo, product.getProductInfo());
    }

    @Test
    @DisplayName("제품 정보 수정 실패")
    void changeProductInfoFailure() {
        assertThrows(ProductValidationException.class, () -> product.changeProductInfo(new ProductInfo(null, 1000)));
        assertThrows(ProductValidationException.class, () -> product.changeProductInfo(new ProductInfo("볼펜", -500)));
    }

    @Test
    @DisplayName("제품 수량 수정 성공")
    void changeQuantitySuccess() {

        Quantity quantity = product.getQuantity();

        Integer changeQuantity = 30;

        product.changeQuantity(changeQuantity);

        assertEquals(30, product.getQuantity().getQuantity());
        assertNotEquals(quantity, product.getQuantity());
    }

    @Test
    @DisplayName("제품 수량 수정 실패")
    void changeQuantityFailure() {
        assertThrows(ProductValidationException.class, () -> product.changeQuantity(-100));
    }

}