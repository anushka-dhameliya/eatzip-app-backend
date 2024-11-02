package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.MenuItem;
import com.eatzip.service.menuItem.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menuItems")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id,
                                                    @RequestHeader("Authorization") String jwtToken) throws CustomException {
        MenuItem menuItem = menuItemService.findMenuItemById(id);

        return new ResponseEntity<>(menuItem, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long restaurantId,
                                                       @RequestHeader("Authorization") String jwtToken) throws CustomException{
        List<MenuItem> menuItems = menuItemService.getAllMenuItemsByRestaurant(restaurantId);

        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MenuItem>> filterMenuItemsByRestaurant(@RequestParam(name = "restaurantId", required = false) Long restaurantId,
                                                                      @RequestParam(name = "name", required = false) String name,
                                                                      @RequestParam(name = "isVegetarian", required = false) String isVegetarian,
                                                                      @RequestParam(name = "category", required = false) String category) throws CustomException {
        if(restaurantId != 0L){
            List<MenuItem> menuItems = menuItemService.filterMenuItemsByRestaurant(restaurantId, name, isVegetarian, category);
            return new ResponseEntity<>(menuItems, HttpStatus.OK);
        }

        List<MenuItem> menuItems = menuItemService.filterMenuItems(name, isVegetarian, category);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }


}
