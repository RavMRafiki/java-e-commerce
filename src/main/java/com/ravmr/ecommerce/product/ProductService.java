package com.ravmr.ecommerce.product;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ravmr.ecommerce.product.ProductResponseDtos.AttributeValueResponseDto;
import com.ravmr.ecommerce.product.ProductResponseDtos.ProductResponseDto;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
    }

    public List<ProductResponseDto> getAllProducts(String category, Double minPrice, Double maxPrice) {
        Predicate<Product> commonFilter = matchesCommonFilters(category, minPrice, maxPrice);

        return productRepository.findAll()
                .stream()
            .filter(commonFilter)
                .map(this::toDto)
                .toList();
    }

    public List<ProductResponseDto> getProductsByCategory(String category, Map<String, String> filters) {
        logger.debug("Filtering products by category: {} with filters: {}", category, filters);
        Double minPrice = filters.containsKey("minPrice") ? Double.valueOf(filters.get("minPrice")) : null;
        Double maxPrice = filters.containsKey("maxPrice") ? Double.valueOf(filters.get("maxPrice")) : null;
        filters.remove("minPrice");
        filters.remove("maxPrice");
        Predicate<Product> commonFilter = matchesCommonFilters(category, minPrice, maxPrice);

        return productRepository.findAll()
                .stream()
                .filter(commonFilter)
                .filter(product -> matchesAttributeFilters(product, filters))
                .map(this::toDto)
                .toList();
    }

    private ProductResponseDto toDto(Product product) {

        List<AttributeValueResponseDto> attributeValues = product.getAttributeValues().stream().map(av -> new AttributeValueResponseDto(
                av.getAttribute().getName(),
            attributeValueToString(av)
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

    private String attributeValueToString(ProductAttributeValue attributeValue) {
        if (attributeValue.getStringValue() != null) {
            return attributeValue.getStringValue();
        }
        if (attributeValue.getNumericValue() != null) {
            return attributeValue.getNumericValue().toString();
        }
        if (attributeValue.getBooleanValue() != null) {
            return attributeValue.getBooleanValue().toString();
        }
        return null;
    }

    private Predicate<Product> matchesCommonFilters(String category, Double minPrice, Double maxPrice) {
        return product -> matchesCategory(product, category)
                && matchesPrice(product, minPrice, maxPrice);
    }

    private boolean matchesCategory(Product product, String category) {
        return category == null
                || (product.getCategory() != null && product.getCategory().getName().equalsIgnoreCase(category));
    }

    private boolean matchesPrice(Product product, Double minPrice, Double maxPrice) {
        return (minPrice == null || product.getPrice() >= minPrice)
                && (maxPrice == null || product.getPrice() <= maxPrice);
    }

    private boolean matchesAttributeFilters(Product product, Map<String, String> filters) {
        if (filters.isEmpty()) {
            return true;
        }

        return filters.entrySet().stream().allMatch(filter -> {
            var attributeValue = product.getAttributeValues().stream()
                    .filter(av -> av.getAttribute().getName().equalsIgnoreCase(filter.getKey()))
                    .findFirst();
            String[] filterValues = filter.getValue().split(",");
            return ProductUtils.isMatchValueType(filterValues, attributeValue.orElse(null));
        });
    }


}
