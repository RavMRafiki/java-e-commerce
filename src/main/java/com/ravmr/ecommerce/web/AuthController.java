package com.ravmr.ecommerce.web;

import com.ravmr.ecommerce.auth.AuthService;
import com.ravmr.ecommerce.security.JwtTokenService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtTokenService tokenService;
    private final AuthService authService;

    @Value("${jwt.cookie.name:refresh_token}")
    private String refreshCookieName;
    @Value("${jwt.cookie.path:/}")
    private String refreshCookiePath;
    @Value("${jwt.cookie.secure:false}")
    private boolean refreshCookieSecure;
    @Value("${jwt.cookie.same-site:Lax}")
    private String refreshCookieSameSite;
    @Value("${jwt.cookie.domain:}")
    private String refreshCookieDomain;

    public AuthController(AuthenticationManager authManager, JwtTokenService tokenService, AuthService authService) {
    this.authManager = authManager;
    this.tokenService = tokenService;
    this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.username(), req.password()));
    List<String> roles = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .map(r -> r.replace("ROLE_", ""))
        .toList();

    String accessToken = tokenService.generateAccessToken(auth.getName(), roles);
    String refreshToken = authService.issueRefreshToken(auth.getName());

    ResponseCookie cookie = baseRefreshCookie(refreshToken)
        .maxAge(Duration.ofSeconds(tokenService.getRefreshExpirationSeconds()))
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(Map.of(
            "accessToken", accessToken,
            "tokenType", "Bearer",
            "expiresIn", 900
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refresh_token", required = false) String refreshCookie) {
    if (refreshCookie == null || refreshCookie.isBlank()) {
        return ResponseEntity.status(401).body(Map.of("error", "Missing refresh token"));
    }
    String newRefresh = authService.rotateRefreshToken(refreshCookie);
    ResponseCookie cookie = baseRefreshCookie(newRefresh)
        .maxAge(Duration.ofSeconds(tokenService.getRefreshExpirationSeconds()))
        .build();
    // Access tokens should be short-lived; issue a new one without re-authenticating
    var jws = tokenService.parseExpectingType(newRefresh, "refresh");
    String username = jws.getBody().getSubject();
    String newAccess = tokenService.generateAccessToken(username, List.of("USER"));
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(Map.of("accessToken", newAccess, "tokenType", "Bearer", "expiresIn", 900));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refresh_token", required = false) String refreshCookie) {
    if (refreshCookie != null && !refreshCookie.isBlank()) {
        authService.revokeRefreshToken(refreshCookie);
    }
    ResponseCookie cleared = baseRefreshCookie("")
        .maxAge(Duration.ZERO)
        .build();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cleared.toString())
        .body(Map.of("message", "Logged out"));
    }

    private ResponseCookie.ResponseCookieBuilder baseRefreshCookie(String value) {
    ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(refreshCookieName, value)
        .httpOnly(true)
        .secure(refreshCookieSecure)
        .path(refreshCookiePath);
    if (refreshCookieDomain != null && !refreshCookieDomain.isBlank()) {
        b.domain(refreshCookieDomain);
    }
    // SameSite
    b.sameSite(refreshCookieSameSite);
    return b;
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
}
