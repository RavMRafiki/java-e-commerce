package com.ravmr.ecommerce.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ravmr.ecommerce.product.ProductResponseDtos.AttributeValueResponseDto;
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

        List<AttributeValueResponseDto> attributeValues = product.getAttributeValues().stream().map(av -> new AttributeValueResponseDto(
                av.getAttribute().getName(),
                av.getStringValue() != null ? av.getStringValue() :
                av.getNumericValue() != null ? av.getNumericValue().toString() :
                av.getBooleanValue() != null ? av.getBooleanValue().toString() :
                null
        )).toList();

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getShortDescription(),
                product.getPrice(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                attributeValues
        );
    }
}
