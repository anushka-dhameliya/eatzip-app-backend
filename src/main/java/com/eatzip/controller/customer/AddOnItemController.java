package com.eatzip.controller.customer;

import com.eatzip.exception.CustomException;
import com.eatzip.model.AddOnItem;
import com.eatzip.service.addOnItem.AddOnItemService;
import com.eatzip.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addOnItems")
public class AddOnItemController {

    @Autowired
    private AddOnItemService addOnItemService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{id}")
    public ResponseEntity<AddOnItem> getAddOnItemById(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwtToken) throws CustomException {
        AddOnItem addOnItem = addOnItemService.getAddOnItemById(id);
        return new ResponseEntity<>(addOnItem, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<AddOnItem>> getAddOnItemsByRestaurantId(@PathVariable Long restaurantId,
                                                                       @RequestHeader("Authorization") String jwtToken) throws CustomException{
        List<AddOnItem> addOnItems = addOnItemService.getAddOnItemByRestaurant(restaurantId);
        return new ResponseEntity<>(addOnItems, HttpStatus.OK);
    }
}
