package com.retreatreserve.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${app.api.version:1.0}")
    private String apiVersion;
    
    @Value("${app.server.url:http://localhost:8080}")
    private String serverUrl;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(apiInfo())
            .servers(List.of(
                new Server().url(serverUrl).description("Development Server")
            ))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", securityScheme()))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
    
    private Info apiInfo() {
        return new Info()
            .title("Retreat Reserve API")
            .version(apiVersion)
            .description("""
                REST API for Retreat Reserve - A cabin rental platform.
                
                ## Features
                - User authentication with JWT
                - Cabin search and booking
                - Reservation management
                - Review system
                - Image upload
                - Email notifications
                
                ## Authentication
                Most endpoints require authentication. Use the `/api/v1/auth/login` endpoint to obtain a JWT token,
                then include it in the `Authorization` header as `Bearer {token}`.
                
                ## Rate Limiting
                API requests are rate limited to 100 requests per minute per IP address.
                """)
            .contact(new Contact()
                .name("Retreat Reserve Support")
                .email("retreatreserve@gmail.com"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT"));
    }
    
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Enter JWT token obtained from /api/v1/auth/login");
    }
}
