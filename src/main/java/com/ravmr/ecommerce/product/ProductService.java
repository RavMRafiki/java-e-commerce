package com.ravmr.ecommerce.product;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ravmr.ecommerce.product.ProductResponseDtos.AttributeValueResponseDto;
import com.ravmr.ecommerce.product.ProductResponseDtos.ProductResponseDto;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDto> getAllProducts(String category, Double minPrice, Double maxPrice) {
        return productRepository.findAll()
                .stream()
                .filter(p -> (category == null || (p.getCategory() != null && p.getCategory().getName().equalsIgnoreCase(category))) &&
                             (minPrice == null || p.getPrice() >= minPrice) &&
                             (maxPrice == null || p.getPrice() <= maxPrice))
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

    public List<ProductResponseDto> getProductsByCategory(String category, Map<String, String> filters) {
        System.out.println("Filtering products by category: " + category + " with filters: " + filters);
        Double minPrice = filters.containsKey("minPrice") ? Double.valueOf(filters.get("minPrice")) : null;
        Double maxPrice = filters.containsKey("maxPrice") ? Double.valueOf(filters.get("maxPrice")) : null;
        filters.remove("minPrice");
        filters.remove("maxPrice");
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getName().equalsIgnoreCase(category))
                .filter(p -> (category == null || (p.getCategory() != null && p.getCategory().getName().equalsIgnoreCase(category))) &&
                             (minPrice == null || p.getPrice() >= minPrice) &&
                             (maxPrice == null || p.getPrice() <= maxPrice))
                .filter(p -> filters.isEmpty() || filters.entrySet().stream().allMatch(f -> {
                    System.out.println(f);
                    var attributeValue = p.getAttributeValues().stream()
                            .filter(av -> av.getAttribute().getName().equalsIgnoreCase(f.getKey()))
                            .findFirst();
                    String[] filterValues = f.getValue().split(",");
                    return ProductUtils.isMatchValueType(filterValues, attributeValue.orElse(null));
                }))
                .map(this::toDto)
                .toList();
    }
}
