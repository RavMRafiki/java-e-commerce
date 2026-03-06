package com.ravmr.ecommerce.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ravmr.ecommerce.user.exeception.UsernameAlreadyExistsException;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    User registerUser(String username, String email, String firstName, String lastName, String password) {
        if (repo.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setActivated(false);
        user.setPasswordHash(passwordEncoder.encode(password));
        return repo.createNewUser(user);
    }

    @Transactional
    User getUserByUsername(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Transactional
    public User getUserFromContext() {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }

    @Transactional
    public User getUserById(Long userId) {
        return repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}