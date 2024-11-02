package com.eatzip.service.order;


import com.eatzip.exception.CustomException;
import com.eatzip.model.*;
import com.eatzip.repository.AddressRepository;
import com.eatzip.repository.OrderItemRepository;
import com.eatzip.repository.OrderRepository;
import com.eatzip.repository.UserRepository;
import com.eatzip.request.CreateOrderRequest;
import com.eatzip.service.cart.CartService;
import com.eatzip.service.offer.OfferService;
import com.eatzip.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OfferService offerService;


    @Override
    public Order createOrder(CreateOrderRequest orderRequest, User user) throws CustomException {
        try{

            Order order = Order.builder()
                    .deliveryAddress(orderRequest.getDeliveryAddress())
                    .customer(user)
                    .restaurant(orderRequest.getRestaurant())
                    .orderStatus(OrderStatus.NEW)
                    .orderDate(LocalDateTime.now())
                    .build();

            /*Address address = orderRequest.getDeliveryAddress();
            if(address!= null && address.getId() != null && address.getId() != 0L){
                Optional<Address> addressDB = addressRepository.findById(address.getId());
                if(addressDB.isPresent()){
                    if(!user.getAddresses().contains(addressDB.get())){
                        order.setDeliveryAddress(addressDB.get());
                        user.getAddresses().add(addressDB.get());
                    }
                }
            }
            else{
                Address savedAddress = addressRepository.save(address);
                user.getAddresses().add(savedAddress);
                order.setDeliveryAddress(savedAddress);
            }*/


            Cart cart = cartService.findCartByUserId(user.getId());
            Long totalAmount = cartService.calculateCartTotal(cart);
            order.setTotalAmount(totalAmount);
            order.setTotalItems(cart.getTotalItems());

            if(orderRequest.getOffer() != null){
                order.setOffer(orderRequest.getOffer());
                Long total =  offerService.applyOffer(orderRequest.getOffer(), cart);
                order.setTotalAmount(total);
            }

            System.out.println("totalAmount : " + order.getTotalAmount());
            System.out.println("TotalItems : " + order.getTotalItems());

            Order savedOrder = orderRepository.save(order);

            List<OrderItem> orderItems = new ArrayList<>();

            for(CartItem item : cart.getCartItems()){
                OrderItem orderItem = OrderItem.builder()
                        .menuItem(item.getMenuItem())
                        .addOnItems(new ArrayList<>())
                        .quantity(item.getQuantity())
                        .totalAmount(item.getTotalAmount())
                        .build();
                if(item.getAddOnItems() != null && !item.getAddOnItems().isEmpty()){
                    for(AddOnItem addOnItem : item.getAddOnItems()){
                        orderItem.getAddOnItems().add(addOnItem);
                    }
                }
                orderItem.setOrder(savedOrder);
                orderItems.add(orderItem);

                if(savedOrder.getOrderItems() == null)
                    savedOrder.setOrderItems(new ArrayList<>());

                savedOrder.getOrderItems().add(orderItem);
            }

            savedOrder = orderRepository.save(savedOrder);

            cartService.clearCart(user.getId());

            System.out.println("totalAmount after cart update : " + order.getTotalAmount());
            System.out.println("TotalItems after cart update : " + order.getTotalItems());


            Restaurant restaurant = restaurantService.findRestaurantById(savedOrder.getRestaurant().getId());
            restaurant.getOrders().add(savedOrder);

            user.getOrders().add(savedOrder);
            userRepository.save(user);

            return savedOrder;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new CustomException("Error while creating order...");
        }
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) throws CustomException {
        Order order = getOrderById(orderId);
        order.setOrderStatus(status);
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    @Override
    public void cancelOrder(Long orderId) throws CustomException {
        Order order = getOrderById(orderId);
        for(OrderItem item : order.getOrderItems()){
            deleteOrderItems(item.getId());
        }
        orderRepository.deleteById(order.getId());
    }

    public void deleteOrderItems(Long orderItemsId) throws CustomException{
        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemsId);
        orderItem.ifPresent(item -> orderItemRepository.deleteById(item.getId()));
    }

    @Override
    public Order getOrderById(Long orderId) throws CustomException {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isEmpty()){
            throw new CustomException("Order not found with id = " + orderId);
        }
        return order.get();
    }

    @Override
    public List<Order> getAllOrdersForUser(Long userId) throws CustomException {
        return orderRepository.findByCustomer_IdOrderByIdDesc(userId);
    }

    @Override
    public List<Order> filterOrderForCustomer(Long userId, OrderStatus status) throws CustomException {
        List<Order> orders = getAllOrdersForUser(userId);
        return orders.stream().filter(i -> i.getOrderStatus().equals(status)).toList();
    }

    @Override
    public List<Order> getAllOrdersForRestaurant(Long restaurantId) throws CustomException {
        return orderRepository.findByRestaurant_IdOrderByIdDesc(restaurantId);
    }

    @Override
    public List<Order> filterOrderForRestaurant(Long restaurantId, OrderStatus status) throws CustomException {
        List<Order> orders = getAllOrdersForRestaurant(restaurantId);
        return orders.stream().filter(i -> i.getOrderStatus().equals(status)).toList();
    }
}
