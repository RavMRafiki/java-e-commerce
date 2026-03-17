package com.ravmr.ecommerce.order;

import java.util.Date;
import java.util.List;

import com.ravmr.ecommerce.user.DeliveryResponseDtos;

public class OrderResponseDtos {
    public record OrderResponseDto(
            Long orderId,
            Long userId,
            List<OrderProductResponseDto> products,
            Date orderDate,
            String status,
            DeliveryResponseDtos.DeliveryShortDto delivery
    ) {
    }

    public record OrderProductResponseDto(
            Long productId,
            String productName,
            Double productPrice,
            Integer quantity
    ) {
    }

    static OrderResponseDto toDto(Order order, List<OrderProduct> orderProducts) {
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
                order.getStatus(),
                DeliveryResponseDtos.toShortDto(order.getDelivery())
        );
    }
}