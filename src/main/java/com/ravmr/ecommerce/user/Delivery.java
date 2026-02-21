package com.ravmr.ecommerce.user;

import jakarta.persistence.*;

@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isDefault;

    @Column(nullable = false, length = 256)
    private String name;

    @Column(nullable = true, length = 256)
    private String street;

    @Column(nullable = false, length = 256)
    private String houseNumber;

    @Column(nullable = true, length = 256)
    private String apartmentNumber;

    @Column(nullable = false, length = 256)
    private String city;

    @Column(nullable = false, length = 10)
    private String postalCode;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
