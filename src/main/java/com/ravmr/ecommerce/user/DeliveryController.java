package com.ravmr.ecommerce.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final UserService userService;

    public DeliveryController(DeliveryService deliveryService, UserService userService) {
        this.deliveryService = deliveryService;
        this.userService = userService;
    }

    @PostMapping("/delivery")
    public ResponseEntity<?> addDeliveryInfo(@Valid @RequestBody DeliveryRequest req) {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        
        User user = userService.getUserByUsername(username);

        deliveryService.addDeliveryInfo(user, req);
        return ResponseEntity.ok(Map.of("message", "Delivery information added successfully"));
    }

    public record DeliveryRequest(
            String name,
            String street,
            @NotBlank String houseNumber,
            String apartmentNumber,
            @NotBlank String city,
            @NotBlank String postalCode,
            @NotBlank String phoneNumber
    ) {}

    @DeleteMapping("/delivery/{id}")
    public ResponseEntity<?> removeDeliveryInfo(@PathVariable Long id) {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        deliveryService.removeDeliveryInfo(id, user);
        return ResponseEntity.ok(Map.of("message", "Delivery information removed successfully"));
    }

    public record DeliveryRequestUpdate(
            String name,
            String street,
            String houseNumber,
            String apartmentNumber,
            String city,
            String postalCode,
            String phoneNumber
    ) {}

    @PatchMapping("/delivery/{id}")
    public ResponseEntity<?> updateDeliveryInfo(@PathVariable Long id, @RequestBody DeliveryRequestUpdate req) {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        deliveryService.updateDeliveryInfo(id, user, req);
        return ResponseEntity.ok(Map.of("message", "Delivery information updated successfully"));
    }

    @PutMapping("/delivery/default/{deliveryId}")
    public ResponseEntity<?> setDefaultDeliveryInfo(@PathVariable Long deliveryId) {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        Delivery delivery = deliveryService.getDefaultDeliveryForUser(user.getId());
        if (!delivery.getId().equals(deliveryId)) {
            deliveryService.setDefaultDeliveryInfo(deliveryId, user);
        }
        return ResponseEntity.ok(Map.of("message", "Default delivery information set successfully"));
    }
}