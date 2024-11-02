package com.eatzip.service.order;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Order;
import com.eatzip.model.OrderStatus;
import com.eatzip.model.User;
import com.eatzip.request.CreateOrderRequest;

import java.util.List;

public interface OrderService {

    public Order createOrder(CreateOrderRequest orderRequest, User user) throws CustomException;

    public Order updateOrderStatus(Long orderId, OrderStatus status) throws CustomException;

    public void cancelOrder(Long orderId) throws CustomException;

    public Order getOrderById(Long orderId) throws CustomException;

    public List<Order> getAllOrdersForUser(Long userId) throws CustomException;

    public List<Order> filterOrderForCustomer(Long userId, OrderStatus status) throws CustomException;

    public List<Order> getAllOrdersForRestaurant(Long restaurantId) throws CustomException;

    public List<Order> filterOrderForRestaurant(Long restaurantId, OrderStatus status) throws CustomException;
}
