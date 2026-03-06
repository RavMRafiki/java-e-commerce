package com.ravmr.ecommerce.cart;

public class CartResponseDtos {
    public record CartResponseDto(
            Long id,
            Long productId,
            String productName,
            String productShortDescription,
            Double productPrice,
            String categoryName,
            Integer quantity
    ) {
    }
}
