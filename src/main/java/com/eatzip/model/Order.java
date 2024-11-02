package com.eatzip.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    //@JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "offer_id", nullable = true)
    private Offer offer;

    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private Address deliveryAddress;

    private int totalItems;

    private double totalAmount;

    private OrderStatus orderStatus;
}
