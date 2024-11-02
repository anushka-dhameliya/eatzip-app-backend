package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Restaurant;
import com.eatzip.service.restaurant.RestaurantService;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants(){
        return new ResponseEntity<>(restaurantService.getAllRestaurants(), HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long restaurantId,
                                                        @RequestHeader("Authorization") String jwtToken) throws CustomException {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/restaurants/filter")
    public ResponseEntity<List<Restaurant>> searchRestaurantsBasedOnFilter(@RequestParam(name = "open", required = false) String open,
                                                                           @RequestParam(name = "name", required = false) String name,
                                                                           @RequestParam(name = "isVegetarian", required = false) String isVegetarian,
                                                                           @RequestParam(name = "category", required = false) String category,
                                                                           @RequestParam(name = "city", required = false) String city,
                                                                           @RequestParam(name = "pinCode", required = false) String pinCode){
        return new ResponseEntity<>(restaurantService.searchRestaurantsBasedOnFilter(name, open, isVegetarian, city, pinCode, category), HttpStatus.OK);
    }
}
