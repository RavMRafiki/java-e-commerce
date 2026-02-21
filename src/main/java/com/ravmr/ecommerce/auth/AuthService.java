package com.ravmr.ecommerce.auth;

import com.ravmr.ecommerce.security.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    private final JwtTokenService jwt;
    private final RefreshTokenRepository repo;

    public AuthService(JwtTokenService jwt, RefreshTokenRepository repo) {
        this.jwt = jwt;
        this.repo = repo;
    }

    @Transactional
    public String issueRefreshToken(String userId) {
        String jti = UUID.randomUUID().toString();
        String token = jwt.generateRefreshToken(userId, jti);
        Jws<Claims> jws = jwt.parseExpectingType(token, "refresh");
        RefreshToken rt = new RefreshToken();
        rt.setJti(jti);
        rt.setUserId(userId);
        rt.setCreatedAt(Instant.now());
        rt.setExpiresAt(jws.getBody().getExpiration().toInstant());
        repo.save(rt);
        return token;
    }

    @Transactional
    public String rotateRefreshToken(String rawToken) throws JwtException {
        Jws<Claims> jws = jwt.parseExpectingType(rawToken, "refresh");
        String jti = jws.getBody().getId();
        RefreshToken existing = repo.findByJtiAndRevokedFalse(jti)
                .orElseThrow(() -> new JwtException("Refresh token not found or revoked"));
        existing.setRevoked(true);
        existing.setRevokedAt(Instant.now());
        repo.save(existing);

        String userId = jws.getBody().getSubject();
        return issueRefreshToken(userId);
    }

    @Transactional
    public void revokeRefreshToken(String rawToken) {
        try {
            Jws<Claims> jws = jwt.parseExpectingType(rawToken, "refresh");
            String jti = jws.getBody().getId();
            repo.findByJtiAndRevokedFalse(jti).ifPresent(rt -> {
                rt.setRevoked(true);
                rt.setRevokedAt(Instant.now());
                repo.save(rt);
            });
        } catch (JwtException ignored) {
            // invalid or expired token: nothing to revoke
        }
    }
}
