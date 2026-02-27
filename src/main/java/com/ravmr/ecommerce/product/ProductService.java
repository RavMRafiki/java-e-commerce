package com.ravmr.ecommerce.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ravmr.ecommerce.product.ProductResponseDtos.ProductResponseDto;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getShortDescription(),
                product.getPrice(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                "product.getAttributeValues().iterator().next().getAttribute().getName()"
        );
    }

}
