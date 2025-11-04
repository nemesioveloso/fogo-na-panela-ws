package com.example.base.security;

import com.example.base.enums.Role;
import com.example.base.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JWTService {

    @Value("${security.jwt.secret}")
    private String base64Secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.access-token-minutes}")
    private int accessMinutes;

    @Value("${security.jwt.refresh-token-days}")
    private int refreshDays;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    private void addCommonClaims(JwtBuilder builder, User user) {
        builder.setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().stream().map(Enum::name).toList());
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder();

        addCommonClaims(builder, user);

        return builder
                .claim("token_type", "access")
                .setHeaderParam("kid", "default")
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(accessMinutes, ChronoUnit.MINUTES)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder();

        addCommonClaims(builder, user);

        return builder
                .claim("token_type", "refresh")
                .setIssuer(issuer)
                .setId(UUID.randomUUID().toString())
                .setExpiration(Date.from(now.plus(refreshDays, ChronoUnit.DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(30)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    public Long extractUserId(String token) {
        return parseClaims(token).get("id", Number.class).longValue();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isRefreshToken(String token) {
        try {
            Claims claims = parseClaims(token);
            String type = claims.get("token_type", String.class);
            return "refresh".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    public Set<Role> extractRoles(String token) {
        List<?> rawRoles = parseClaims(token).get("roles", List.class);

        if (rawRoles == null) return Set.of();

        return rawRoles.stream()
                .filter(String.class::isInstance) // 🔐 garante que só convertemos Strings
                .map(String.class::cast)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}