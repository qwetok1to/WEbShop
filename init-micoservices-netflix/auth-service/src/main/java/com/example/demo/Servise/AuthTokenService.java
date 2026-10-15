package com.example.demo.Servise;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.AuthDTOS;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthTokenService {

    private final SecretKey signingKey;
    private final long expirationSeconds;

    public AuthTokenService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        if (expirationSeconds <= 0) {
            throw new IllegalArgumentException("JWT expiration must be greater than zero");
        }
        this.expirationSeconds = expirationSeconds;
    }

    public String createToken(AuthDTOS.UserResponse user) {
        Instant issuedAt = Instant.now();
        return Jwts.builder()
                .subject(user.id())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(issuedAt.plusSeconds(expirationSeconds)))
                .claim("roles", List.of("ROLE_USER"))
                .signWith(signingKey)
                .compact();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
