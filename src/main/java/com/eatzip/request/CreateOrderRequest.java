package com.eatzip.request;


import com.eatzip.model.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CreateOrderRequest {

    private User customer;
    private Restaurant restaurant;
    private List<OrderItem> orderItems;
    private Offer offer;
    private LocalDateTime orderDate;
    private int totalItems;
    private double totalAmount;
    private OrderStatus orderStatus;
    private Address deliveryAddress;
}
