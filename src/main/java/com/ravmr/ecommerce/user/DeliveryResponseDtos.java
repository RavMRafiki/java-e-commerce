package com.ravmr.ecommerce.user;

import java.util.List;

public class DeliveryResponseDtos {
    public record DeliveryResponseDto(
            Long id,
            String name,
            String street,
            String houseNumber,
            String apartmentNumber,
            String city,
            String postalCode,
            String phoneNumber,
            boolean isDefault
    ) {
    }

    public record DeliveryListResponseDto(
            List<DeliveryResponseDto> deliveries
    ) {
    }
}
