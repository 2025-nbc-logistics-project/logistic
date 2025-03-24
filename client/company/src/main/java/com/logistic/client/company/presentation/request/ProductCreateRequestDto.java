package com.logistic.client.company.presentation.request;

import com.logistic.client.company.domain.model.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "상품 등록 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {

    @Schema(description = "소속 업체 UUID", example = "e2eea1ab-463e-42e7-82d5-0da14235fe5a")
    @NotNull(message = "소속 업체를 입력해주세요.")
    private UUID companyId;

    @Schema(description = "소속 허브 UUID", example = "e4f3d8b1-8a5b-48a7-8f69-9c44de0a967f")
    @NotNull(message = "소속 허브를 입력해주세요.")
    private UUID hubId;

    @Schema(description = "상품 이름", example = "연필")
    @NotBlank(message = "상품 이름을 입력해주세요.")
    private String productName;

    @Schema(description = "상품 가격", example = "500")
    @Min(value = 0, message = "가격은 0원 이상이여야 합니다.")
    private int price;

    @Schema(description = "상품 개수", example = "100")
    @Min(value = 0, message = "개수는 0개 이상이여야 합니다.")
    private int quantity;

    public ProductCreateRequestDto(Product product) {
        this.companyId = product.getCompanyId();
        this.hubId = product.getHubId();
        this.productName = product.getProductInfo().getProductName();
        this.price = product.getProductInfo().getPrice();
        this.quantity = product.getQuantity().getQuantity();
    }

}
