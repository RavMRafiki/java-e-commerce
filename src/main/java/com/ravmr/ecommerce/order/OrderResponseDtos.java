package com.ravmr.ecommerce.order;

import java.util.Date;
import java.util.List;

public class OrderResponseDtos {
    public record OrderResponseDto(
            Long orderId,
            Long userId,
            List<OrderProductResponseDto> products,
            Date orderDate,
            String status
    ) {
    }

    public record OrderProductResponseDto(
            Long productId,
            String productName,
            Double productPrice,
            Integer quantity
    ) {
    }

    OrderResponseDto toDto(Order order, List<OrderProduct> orderProducts) {
        return new OrderResponseDto(
                order.getId(),
                order.getUser().getId(),
                order.getOrderProducts().stream().map(orderProduct -> new OrderProductResponseDto(
                        orderProduct.getProduct().getId(),
                        orderProduct.getProduct().getName(),
                        orderProduct.getUnitPrice(),
                        orderProduct.getQuantity()
                )).toList(),
                order.getOrderDate(),
                order.getStatus()
        );
    }
}