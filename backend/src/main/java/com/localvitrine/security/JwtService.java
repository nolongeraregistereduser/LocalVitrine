package com.localvitrine.security;

import com.localvitrine.config.JwtProperties;
import com.localvitrine.entity.RoleName;
import com.localvitrine.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    public static final String ROLE_CLAIM = "role";

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(User user) {
        return buildToken(user.getEmail(), user.getRole().getName());
    }

    /**
     * Exposed for tests and any flow that already validated identity outside JPA.
     */
    public String generateToken(String email, RoleName roleName) {
        return buildToken(email, roleName);
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ignored) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        Claims claims = parseClaims(token);
        String role = claims.get(ROLE_CLAIM, String.class);
        if (role == null || role.isBlank()) {
            role = RoleName.USER.name();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String buildToken(String email, RoleName roleName) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + jwtProperties.expirationMs());
        return Jwts.builder()
                .subject(email)
                .claim(ROLE_CLAIM, roleName.name())
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(signingKey())
                .compact();
    }

    private SecretKey signingKey() {
        byte[] keyBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
