package com.logistic.client.company.application.dto.product;

import com.logistic.client.company.domain.model.product.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {

    @NotNull(message = "소속 업체를 입력해주세요.")
    private UUID companyId;

    @NotNull(message = "소속 허브를 입력해주세요.")
    private UUID hudId;

    @NotBlank(message = "상품 이름을 입력해주세요.")
    private String productName;

    @Min(value = 0, message = "가격은 0원 이상이여야 합니다.")
    private int price;

    @Min(value = 0, message = "개수는 0개 이상이여야 합니다.")
    private int quantity;

    public ProductCreateRequestDto(Product product) {
        this.companyId = product.getCompanyId();
        this.hudId = product.getHubId();
        this.productName = product.getProductInfo().getProductName();
        this.price = product.getProductInfo().getPrice();
        this.quantity = product.getQuantity().getQuantity();
    }

}
