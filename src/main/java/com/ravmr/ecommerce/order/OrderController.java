package com.ravmr.ecommerce.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ravmr.ecommerce.user.User;
import com.ravmr.ecommerce.user.UserService;

@Controller("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String getOrders() {
        User user = userService.getUserFromContext();
        return "orders";
    }

    @GetMapping("/{orderId}")
    public String getOrderDetails() {
        return "order-details";
    }

    @PostMapping
    public String createOrder() {
        User user = userService.getUserFromContext();
        return "order-created";
    }

}