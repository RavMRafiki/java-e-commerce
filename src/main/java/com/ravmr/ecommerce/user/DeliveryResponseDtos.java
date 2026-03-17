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

    public record DeliveryShortDto(
            Long id,
            String street,
            String city,
            String postalCode,
            String phoneNumber
    ) {
    }

    public record DeliveryListResponseDto(
            List<DeliveryResponseDto> deliveries
    ) {
    }

    public static DeliveryResponseDto toDto(Delivery delivery) {
        return new DeliveryResponseDtos.DeliveryResponseDto(
                delivery.getId(),
                delivery.getName(),
                delivery.getStreet(),
                delivery.getHouseNumber(),
                delivery.getApartmentNumber(),
                delivery.getCity(),
                delivery.getPostalCode(),
                delivery.getPhoneNumber(),
                delivery.isDefault()
        );
    }

    public static DeliveryShortDto toShortDto(Delivery delivery) {
        return new DeliveryShortDto(
                delivery.getId(),
                delivery.getStreet(),
                delivery.getCity(),
                delivery.getPostalCode(),
                delivery.getPhoneNumber()
        );
    }
}
