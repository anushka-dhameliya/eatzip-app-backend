package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Order;
import com.eatzip.model.OrderStatus;
import com.eatzip.model.Restaurant;
import com.eatzip.model.User;
import com.eatzip.request.CreateOrderRequest;
import com.eatzip.response.MessageResponse;
import com.eatzip.response.PaymentReceiptResponse;
import com.eatzip.response.PaymentResponse;
import com.eatzip.service.notification.NotificationMessage;
import com.eatzip.service.notification.NotificationService;
import com.eatzip.service.order.OrderService;
import com.eatzip.service.payment.PaymentService;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody CreateOrderRequest request,
                                             @RequestHeader("Authorization") String jwtToken) throws CustomException {
        User user = userService.findUserByJwtToken(jwtToken);
        Order savedOrder = orderService.createOrder(request, user);

        String notificationMessageForCustomer = NotificationMessage.ORDER_CONFIRMATION.message.replace("[#ORDER_ID]", "#ORDER_" + String.valueOf(savedOrder.getId()));
        notificationService.pushNotificationForCustomer(notificationMessageForCustomer, user);
        String notificationMessageForOwner = NotificationMessage.ORDER_RESTAURANT_CONFIRMATION.message.replace("[#ORDER_ID]", "#ORDER_" + String.valueOf(savedOrder.getId())).replace("[CUSTOMER_NAME]", savedOrder.getCustomer().getFullName());
        notificationService.pushNotificationForRestaurantOwner(notificationMessageForOwner, savedOrder.getRestaurant());

        try{
            PaymentResponse paymentResponse = paymentService.createPaymentRequest(savedOrder);
            return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            orderService.updateOrderStatus(savedOrder.getId(), OrderStatus.PAYMENT_FAILED);
            throw new CustomException("Payment Failed.");
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<MessageResponse> cancelOrder(@PathVariable Long orderId,
                                                       @RequestHeader("Authorization") String jwtToken) throws CustomException{
        User user = userService.findUserByJwtToken(jwtToken);
        Restaurant restaurant = orderService.getOrderById(orderId).getRestaurant();
        orderService.cancelOrder(orderId);
        MessageResponse response = MessageResponse.builder()
                .message("Order cancel success").build();
        String notificationMessage = NotificationMessage.ORDER_CANCELED.message.replace("[#ORDER_ID]", "#ORDER_" + String.valueOf(orderId));
        notificationService.pushNotificationForCustomer(notificationMessage, user);
        String notificationMessageForOwner = NotificationMessage.ORDER_RESTAURANT_OWNER_CANCELED.message.replace("[#ORDER_ID]", "#ORDER_" + String.valueOf(orderId));
        notificationService.pushNotificationForRestaurantOwner(notificationMessageForOwner, restaurant);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId,
                                                   @RequestHeader("Authorization") String jwtToken) throws CustomException{
        Order order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersForUser(@RequestHeader("Authorization") String jwtToken) throws CustomException{
        User user = userService.findUserByJwtToken(jwtToken);
        List<Order> orders = orderService.getAllOrdersForUser(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Order>> filterOrdersForUser(
            @RequestParam("status") OrderStatus status,
            @RequestHeader("Authorization") String jwtToken) throws CustomException {
        User user = userService.findUserByJwtToken(jwtToken);
        List<Order> orders = orderService.filterOrderForCustomer(user.getId(), status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/payment-receipt/{orderId}")
    public ResponseEntity<PaymentReceiptResponse> getReceiptUrl(@PathVariable Long orderId) throws CustomException{
        try{
            PaymentReceiptResponse response = paymentService.getReceiptUrl(orderId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

}
