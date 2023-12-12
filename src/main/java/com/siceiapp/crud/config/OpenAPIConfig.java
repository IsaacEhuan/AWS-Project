package com.siceiapp.crud.config;

import org.springframework.context.annotation.*;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;

@Configuration
public class OpenAPIConfig {
        @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("siceiapp")
                .version("v0.0.1")
                .description("Proyecto final AWS")
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.siceiapp.com/")
                )
        );
    }
}
