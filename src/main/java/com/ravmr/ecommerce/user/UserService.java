package com.ravmr.ecommerce.user;

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
public class UserService {
    private final UserRepository repo;

    public UserService(JwtTokenService jwt, UserRepository repo) {
        // this.jwt = jwt;
        this.repo = repo;
    }

    @Transactional
    User registerUser(String username, String password) {
        if (repo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(password); // In real code, hash the password!
        return repo.createNewUser(user);
    }
}