package com.logistic.client.order.infrastructure.config;

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
        name = "Authorization",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER
    ),
    @SecurityScheme(
        name = "role",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER
    ),
    @SecurityScheme(
        name = "username",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER
    )
})
public class SwaggerConfig {

    @Bean
    public OpenAPI OpenAPI() {
        Info info = new Info()
            .title("주문(Order) API 문서")
            .version("1.0.0")
            .description("물류 주문 관련 API 명세");
        return new OpenAPI().info(info);
    }
}
