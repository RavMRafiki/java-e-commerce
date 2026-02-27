package com.ravmr.ecommerce.product;


import jakarta.persistence.*;

@Entity
@Table(name = "product_attribute_value")
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @Column(nullable = true, length = 255)
    private String stringValue;

    @Column(nullable = true)
    private Double numericValue;

    @Column(nullable = true)
    private Boolean booleanValue;
}