package com.logistic.client.company.domain.model;

import com.logistic.client.company.application.dto.product.ProductCreateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "product_id", nullable = false, updatable = false, unique = true)
    private UUID productId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Embedded
    private ProductInfo productInfo;

    @Embedded
    private Quantity quantity;

    public Product(ProductCreateRequestDto requestDto){
        this.companyId = requestDto.getCompanyId();
        this.hubId = requestDto.getHudId();
        this.productInfo = new ProductInfo(requestDto.getProductName(), requestDto.getPrice());
        this.quantity = new Quantity(requestDto.getQuantity());
    }

}
