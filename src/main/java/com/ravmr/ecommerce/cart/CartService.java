package com.ravmr.ecommerce.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ravmr.ecommerce.product.Product;
import com.ravmr.ecommerce.product.ProductService;
import com.ravmr.ecommerce.user.UserService;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserService userService;

    public CartService(CartRepository cartRepository, ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
    }


    List<CartResponseDtos.CartResponseDto> getCart(Long userID) {
         return cartRepository.findByUserId(userID).stream()
                .map(this::toDto)
                .toList();
    }

    CartResponseDtos.CartResponseDto toDto(Cart cart) {
        return new CartResponseDtos.CartResponseDto(
                cart.getProduct().getId(),
                cart.getProduct().getName(),
                cart.getProduct().getShortDescription(),
                cart.getProduct().getPrice(),
                cart.getProduct().getCategory().getName(),
                cart.getQuantity()
        );
    }

    @Transactional
    public List<CartResponseDtos.CartResponseDto> updateCart(Long userId, Long productId, Integer quantity) {
        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseGet(() -> {
                    System.err.println("Creating new cart item for user " + userId + " and product " + productId);
                    Cart newCartItem = new Cart();
                    newCartItem.setUser(userService.getUserById(userId));
                    Product product = productService.getProductById(productId);
                    newCartItem.setProduct(product);
                    return newCartItem;
                });
        if (quantity <= 0) {
            cartRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
        }
        return getCart(userId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}
