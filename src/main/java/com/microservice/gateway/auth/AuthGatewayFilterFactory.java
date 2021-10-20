package com.microservice.gateway.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
@Order(-1)
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    @Autowired
    private JwtUtils jwtUtils;

    public AuthGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request=exchange.getRequest();
            ServerHttpResponse response= exchange.getResponse();

            HttpHeaders tokenHeader= request.getHeaders();
            if(tokenHeader.containsKey("Authorization")) {
                String token = tokenHeader.getOrEmpty("Authorization").get(0);
                if (token.startsWith("Bearer ")) {
                    String theToken = token.substring(7, token.length());
                    try{
                        boolean isValid = jwtUtils.isValidToken(theToken);
                        if (isValid) {

                            return chain.filter(exchange);

                        }
                    }catch(ExpiredJwtException | MalformedJwtException e){
                        e.printStackTrace();
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    }
                }
            }
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        });
    }

    public static class Config{}
}
