package com.example.forecast.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Grouped OpenAPI for Weather API
    @Bean
    public GroupedOpenApi weatherApi() {
        return GroupedOpenApi.builder()
                .group("Weather API")
                .pathsToMatch("/weatherService/**")
                .build();
    }
}
