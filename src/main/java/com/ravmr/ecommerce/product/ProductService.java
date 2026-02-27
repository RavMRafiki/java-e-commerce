package com.ravmr.ecommerce.product;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        System.out.println(productRepository.findAll());
        return productRepository.findAll();
    }


}
