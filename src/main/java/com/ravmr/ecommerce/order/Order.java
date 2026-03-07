package com.ravmr.ecommerce.order;

import java.sql.Date;
import java.util.Set;

import com.ravmr.ecommerce.product.Product;
import com.ravmr.ecommerce.user.Delivery;
import com.ravmr.ecommerce.user.User;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "orders",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "order_product_id"})
)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderProduct> orderProducts;

    private Date orderDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private String status;

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Set<OrderProduct> getOrderProducts() { return orderProducts; }
    public void setOrderProducts(Set<OrderProduct> orderProducts) { this.orderProducts = orderProducts; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public Delivery getDelivery() { return delivery; }
    public void setDelivery(Delivery delivery) { this.delivery = delivery; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
