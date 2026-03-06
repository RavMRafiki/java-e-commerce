package com.ravmr.ecommerce.user;

import com.ravmr.ecommerce.cart.Cart;

import jakarta.persistence.*;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_username", columnList = "username", unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(nullable = false, length = 128)
    private String email;

    @Column(nullable = false)
    private boolean activated = false;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(nullable = false, length = 64)
    private String lastName;

    @Column(nullable = false, length = 128)
    private String passwordHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.Set<Delivery> deliveries = new java.util.HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.Set<Cart> cartItems = new java.util.HashSet<>();

    public Long getId() { return id; }
    public String getUsername() { return username;}
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public java.util.Set<Delivery> getDeliveries() { return deliveries; }
    public void setDeliveries(java.util.Set<Delivery> deliveries) { this.deliveries = deliveries; }
    public java.util.Set<Cart> getCartItems() { return cartItems; }
    public void setCartItems(java.util.Set<Cart> cartItems) { this.cartItems = cartItems; }
}
