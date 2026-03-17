package com.ravmr.ecommerce.order;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ravmr.ecommerce.cart.Cart;
import com.ravmr.ecommerce.cart.CartRepository;
import com.ravmr.ecommerce.order.OrderResponseDtos.OrderResponseDto;
import com.ravmr.ecommerce.user.Delivery;
import com.ravmr.ecommerce.user.DeliveryRepository;
import com.ravmr.ecommerce.user.DeliveryService;
import com.ravmr.ecommerce.user.User;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartRepository cartRepository;
    private final DeliveryService deliveryService;
    private final DeliveryRepository deliveryRepository;

    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, CartRepository cartRepository, DeliveryService deliveryService, DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.cartRepository = cartRepository;
        this.deliveryService = deliveryService;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public OrderResponseDto createOrder(User user) {
        List<Cart> cart = cartRepository.findByUserId(user.getId());
        if (cart.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Delivery delivery = deliveryService.getDefaultDeliveryForUser(user.getId());
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date(System.currentTimeMillis()));
        order.setDelivery(delivery);
        order.setStatus("PENDING");
        order.setOrderProducts(new HashSet<>());

        orderRepository.save(order);
        cart.forEach(cartItem -> {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cartItem.getProduct());
            orderProduct.setQuantity(cartItem.getQuantity());
            orderProduct.setUnitPrice(cartItem.getProduct().getPrice());
            order.getOrderProducts().add(orderProduct);
            orderProductRepository.save(orderProduct);
        });
        orderRepository.save(order);
        
        cartRepository.deleteAll(cart);
        return OrderResponseDtos.toDto(order, order.getOrderProducts().stream().toList());
    }

    @Transactional
    public void setOrderDeliveryAddress(User user, Long orderId, Long deliveryId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User is not the owner of this order");
        }
        order.setDelivery(delivery);
        orderRepository.save(order);
    }

    @Transactional
    public List<OrderResponseDto> getOrders(User user) {
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orders.stream()
                .map(order -> OrderResponseDtos.toDto(order, order.getOrderProducts().stream().toList()))
                .toList();
    }

    @Transactional
    public OrderResponseDto getOrder(User user, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this order");
        }
        return OrderResponseDtos.toDto(order, order.getOrderProducts().stream().toList());
    }
}
