package com.eatzip.controller.restaurantOwner;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Restaurant;
import com.eatzip.model.User;
import com.eatzip.request.CreateRestaurantRequest;
import com.eatzip.response.MessageResponse;
import com.eatzip.service.notification.NotificationMessage;
import com.eatzip.service.notification.NotificationService;
import com.eatzip.service.restaurant.RestaurantService;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class RestaurantOwnerController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody CreateRestaurantRequest request,
                                                       @RequestHeader("Authorization") String jwtToken) throws CustomException {
        User user = userService.findUserByJwtToken(jwtToken);
        Restaurant savedRestaurant = restaurantService.createRestaurant(request, user);
        String notification = NotificationMessage.RESTAURANT_CREATION.message.replace("[RESTAURANT_NAME]", savedRestaurant.getName());
        notificationService.pushNotificationForRestaurantOwner(notification, savedRestaurant);
        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long restaurantId,
            @RequestBody CreateRestaurantRequest request, @RequestHeader("Authorization") String jwtToken) throws CustomException{
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurantId, request);
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.CREATED);
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<MessageResponse> deleteRestaurant(@PathVariable Long restaurantId,
                                                            @RequestHeader("Authorization") String jwtToken) throws CustomException{
        restaurantService.deleteRestaurant(restaurantId);
        MessageResponse response = MessageResponse.builder()
                .message("Restaurant deleted successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{restaurantId}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(@PathVariable Long restaurantId,
                                                             @RequestHeader("Authorization") String jwtToken) throws  CustomException{
        Restaurant restaurant = restaurantService.updateRestaurantStatus(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long restaurantId,
                                                        @RequestHeader("Authorization") String jwtToken) throws CustomException{
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> getRestaurantByUserId(@RequestHeader("Authorization") String jwtToken) throws CustomException {
        User user = userService.findUserByJwtToken(jwtToken);
        Restaurant restaurant = restaurantService.findRestaurantByUserId(user.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}
