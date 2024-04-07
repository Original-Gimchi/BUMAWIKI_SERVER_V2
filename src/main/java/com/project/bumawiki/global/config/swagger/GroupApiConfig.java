package com.project.bumawiki.global.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupApiConfig {

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi coinApi() {
        return GroupedOpenApi.builder()
                .group("coin")
                .pathsToMatch("/api/coins/**")
                .build();
    }

    @Bean
    public GroupedOpenApi docsApi() {
        return GroupedOpenApi.builder()
                .group("docs")
                .pathsToMatch("/api/docs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi thumbsUpApi() {
        return GroupedOpenApi.builder()
                .group("thumbsUp")
                .pathsToMatch("/api/thumbs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("*")
                .pathsToMatch("/api/**")
                .build();
    }

}
