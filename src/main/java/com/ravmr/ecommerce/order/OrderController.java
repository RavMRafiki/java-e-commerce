package com.ravmr.ecommerce.order;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ravmr.ecommerce.order.OrderResponseDtos.OrderResponseDto;
import com.ravmr.ecommerce.user.User;
import com.ravmr.ecommerce.user.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public List<OrderResponseDto> getOrders() {
        User user = userService.getUserFromContext();
        return orderService.getOrders(user);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrderDetails(@PathVariable Long orderId) {
        User user = userService.getUserFromContext();
        return orderService.getOrder(user, orderId);
    }

    @PostMapping
    public OrderResponseDto createOrder() {
        User user = userService.getUserFromContext();
        OrderResponseDto order = orderService.createOrder(user);
        return order;
    }

    @PutMapping("/{orderId}/delivery/{deliveryId}")
    public void setOrderDeliveryAddress(@PathVariable Long orderId, @PathVariable Long deliveryId) {
        User user = userService.getUserFromContext();
        orderService.setOrderDeliveryAddress(user, orderId, deliveryId);
    }

}