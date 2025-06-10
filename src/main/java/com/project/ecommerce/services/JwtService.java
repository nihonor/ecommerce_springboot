package com.project.ecommerce.services;

import com.project.ecommerce.config.JwtConfig;
import com.project.ecommerce.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    public String generateAccessToken(User user) {
        final long tokenExpiration = jwtConfig.getAccessTokenExpiration();
        return generateToken(user, tokenExpiration);
    }
    public String generateRefreshToken(User user) {
        final long tokenExpiration = jwtConfig.getRefreshTokenExpiration();
        return generateToken(user, tokenExpiration);
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        }
        catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }
}
