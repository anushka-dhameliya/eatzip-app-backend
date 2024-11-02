package com.eatzip.controller.restaurantOwner;


import com.eatzip.exception.CustomException;
import com.eatzip.model.MenuItem;
import com.eatzip.model.Restaurant;
import com.eatzip.model.User;
import com.eatzip.request.CreateMenuItemRequest;
import com.eatzip.response.MessageResponse;
import com.eatzip.service.menuItem.MenuItemService;
import com.eatzip.service.restaurant.RestaurantService;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menuItems")
public class MenuItemOwnerController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody CreateMenuItemRequest request,
                                                   @RequestHeader("Authorization") String jwtToken) throws CustomException {
        User user = userService.findUserByJwtToken(jwtToken);
        Restaurant restaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        MenuItem savedMenuItem = menuItemService.createMenuItem(request, request.getMenuCategory(), restaurant);

        return new ResponseEntity<>(savedMenuItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
            @RequestBody CreateMenuItemRequest request,
                                                   @RequestHeader("Authorization") String jwtToken) throws CustomException{

        MenuItem updatedMenuItem = menuItemService.updateMenuItem(id, request);

        return new ResponseEntity<>(updatedMenuItem, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteMenuItem(@PathVariable Long id,
                                                          @RequestHeader("Authorization") String jwtToken) throws CustomException{
        menuItemService.deleteMenuItem(id);

        MessageResponse response = MessageResponse.builder()
                .message("Menu Item deleted success").build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id,
                                                          @RequestHeader("Authorization") String jwtToken) throws CustomException{
        MenuItem menuItem = menuItemService.findMenuItemById(id);

        return new ResponseEntity<>(menuItem, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getAllMenuItemsByRestaurant(@PathVariable Long restaurantId,
                                                                      @RequestHeader("Authorization") String jwtToken) throws CustomException {
        List<MenuItem> menuItems = menuItemService.getAllMenuItemsByRestaurant(restaurantId);

        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

}
