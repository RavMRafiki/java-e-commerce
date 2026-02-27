package com.ravmr.ecommerce.product;

public class ProductResponseDtos {
        public record ProductResponseDto(
                Long id,
                String name,
                String shortDescription,
                Double price,
                String categoryName,
                String attribute
        ) {
        }

        public record AttributeValueResponseDto(
                String attributeName
        ) {
        }
}
