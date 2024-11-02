package com.eatzip.controller.restaurantOwner;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Order;
import com.eatzip.model.OrderStatus;
import com.eatzip.model.User;
import com.eatzip.service.notification.NotificationMessage;
import com.eatzip.service.notification.NotificationService;
import com.eatzip.service.order.OrderService;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order")
public class OrderOwnerController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId,
                                                   @RequestParam(name = "orderStatus") String orderStatus,
                                                   @RequestHeader("Authorization") String jwtToken) throws CustomException {
        Order updatedOrder = orderService.updateOrderStatus(orderId, OrderStatus.valueOf(orderStatus));
        User customer = updatedOrder.getCustomer();
        if(updatedOrder.getOrderStatus().equals(OrderStatus.COMPLETE)){
            String notificationMessage = NotificationMessage.ORDER_DISPATCHED.message;
            notificationService.pushNotificationForCustomer(notificationMessage, customer);
        }
        else if(updatedOrder.getOrderStatus().equals(OrderStatus.DELIVERED)){
            String notificationMessage = NotificationMessage.ORDER_DELIVERED.message;
            notificationService.pushNotificationForCustomer(notificationMessage, customer);
            String notificationMessageForOwner = NotificationMessage.ORDER_RESTAURANT_DELIVERED.message.replace("#[ORDER_ID]", "#ORDER_" + String.valueOf(updatedOrder.getId())).replace("[CUSTOMER_NAME]", customer.getFullName());
            notificationService.pushNotificationForRestaurantOwner(notificationMessageForOwner, updatedOrder.getRestaurant());
        }
        else if(updatedOrder.getOrderStatus().equals(OrderStatus.REJECTED)){
            String notificationMessage = NotificationMessage.ORDER_CANCELED.message.replace("[#ORDER_ID]", "#ORDER_" + String.valueOf(updatedOrder.getId()));
            notificationService.pushNotificationForCustomer(notificationMessage, customer);
        }
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId,
                                                    @RequestHeader("Authorization") String jwtToken) throws CustomException{
        return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getAllOrdersForRestaurant(@PathVariable Long restaurantId,
                                              @RequestHeader("Authorization") String jwtToken) throws CustomException{
        return new ResponseEntity<>(orderService.getAllOrdersForRestaurant(restaurantId), HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}/filter")
    public ResponseEntity<List<Order>> filterOrdersForRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam("status") OrderStatus status,
            @RequestHeader("Authorization") String jwtToken) throws CustomException {
        List<Order> orders = orderService.filterOrderForRestaurant(restaurantId, status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
