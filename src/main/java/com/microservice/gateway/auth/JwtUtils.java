package com.microservice.gateway.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String secretKey;

    public Claims getClaims(String token) throws MalformedJwtException {

        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(secretKey.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isTokenExpired(String token) throws ExpiredJwtException {
        return getExpirationDate(token).before(new Date(System.currentTimeMillis()));
    }
    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    public boolean isValidToken(String token){
         return (!isTokenExpired(token));
    }
}
