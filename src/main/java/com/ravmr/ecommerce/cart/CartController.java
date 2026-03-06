package com.ravmr.ecommerce.cart;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ravmr.ecommerce.user.UserService;

import jakarta.validation.Valid;

import com.ravmr.ecommerce.user.User;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getCart() {
        User user = userService.getUserFromContext();
        List<CartResponseDtos.CartResponseDto> cartItems = cartService.getCart(user.getId());
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping
    public ResponseEntity<?> updateCart(@Valid @RequestBody UpdateCartRequest request) {
        User user = userService.getUserFromContext();
        return ResponseEntity.ok(cartService.updateCart(user.getId(), request.productId(), request.quantity()));
    }

    public record UpdateCartRequest(
            Long productId,
            Integer quantity
    ) {}

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromCart(@RequestParam Long productId) { 
        User user = userService.getUserFromContext();
        cartService.updateCart(user.getId(), productId, 0);
        return ResponseEntity.ok("Removed from cart");
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart() {
        User user = userService.getUserFromContext();
        cartService.clearCart(user.getId());

        return ResponseEntity.ok("Cleared cart");
    }
}
