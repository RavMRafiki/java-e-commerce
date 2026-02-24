package com.ravmr.ecommerce.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ravmr.ecommerce.auth.RefreshToken;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    default User createNewUser(User user) {
        return save(user);
    }
    
}
