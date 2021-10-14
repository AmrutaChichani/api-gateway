package com.microservice.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class gatewayConfig {
    @Bean
   public RouteLocator apiRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route(predicateSpec ->
                        predicateSpec.path("/api/v1/book/**","/api/v1/author/**","/api/v1/publisher/**")
                        .uri("http://localhost:8082"))
                .route(predicateSpec -> predicateSpec.path("/api/v1/customer/**","/api/v1/auth/**")
                        .uri("http://localhost:8081"))
                .route(predicateSpec -> predicateSpec.path("/api/v1/order/**","/api/v1/cart/**")
                        .uri("http://localhost:8083"))
                .build();

    }
}
