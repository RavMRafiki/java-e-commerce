package com.ravmr.ecommerce.product;

import java.util.List;

public class ProductResponseDtos {
        public record ProductResponseDto(
                Long id,
                String name,
                String shortDescription,
                Double price,
                String categoryName,
                List<AttributeValueResponseDto> attributes
        ) {
        }

        public record AttributeValueResponseDto(
                String attributeName,
                String attributeValue
        ) {
        }
}
