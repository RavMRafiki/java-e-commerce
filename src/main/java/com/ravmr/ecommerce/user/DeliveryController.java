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

        System.out.println("Adding delivery info for user: " + req);
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
}
