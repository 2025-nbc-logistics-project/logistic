package com.logistic.client.company.infrastructure.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecuritySchemes({
        @SecurityScheme(
                name = "userId",
                type = SecuritySchemeType.APIKEY,
                in = SecuritySchemeIn.HEADER
        ),
        @SecurityScheme(
                name = "username",
                type = SecuritySchemeType.APIKEY,
                in = SecuritySchemeIn.HEADER
        ),
        @SecurityScheme(
                name = "role",
                type = SecuritySchemeType.APIKEY,
                in = SecuritySchemeIn.HEADER
        )
})
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){

        Info info = new Info()
                .title("물류 관리 및 배송 시스템")
                .version("1.0")
                .description("업체 및 상품 API Docs");

        return new OpenAPI()
                .info(info);
    }
}
