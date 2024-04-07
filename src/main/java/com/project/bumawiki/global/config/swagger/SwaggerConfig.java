package com.project.bumawiki.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .components(new Components())
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .version("v2.0")
                .title("Api Docs")
                .summary("summary")
                .description("부마위키 스웨거 API 문서");
    }
}
