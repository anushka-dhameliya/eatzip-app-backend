package com.eatzip.controller.restaurantOwner;


import com.eatzip.exception.CustomException;
import com.eatzip.model.AddOnItem;
import com.eatzip.model.Restaurant;
import com.eatzip.request.CreateAddOnItemRequest;
import com.eatzip.response.MessageResponse;
import com.eatzip.service.addOnItem.AddOnItemService;
import com.eatzip.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/addOnItems")
public class AddOnItemOwnerController {

    @Autowired
    private AddOnItemService addOnItemService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<AddOnItem> createAddOnItem(@RequestBody CreateAddOnItemRequest request,
                                                     @RequestHeader("Authorization") String jwtToken) throws CustomException {
        Long restaurantId = request.getRestaurantId();
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        AddOnItem savedAddOnItem = addOnItemService.createAddOnItem(request, restaurant);
        return new ResponseEntity<>(savedAddOnItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddOnItem> updateAddOnItem(@PathVariable Long id,
                                                     @RequestBody CreateAddOnItemRequest request,
                                                     @RequestHeader("Authorization") String jwtToken) throws CustomException{
        AddOnItem updatedAddOnItem = addOnItemService.updateAddOnItem(id, request);
        return new ResponseEntity<>(updatedAddOnItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteAddOnItem(@PathVariable Long id,
                                                           @RequestHeader("Authorization") String jwtToken) throws CustomException{
        addOnItemService.deleteAddOnItem(id);
        MessageResponse response = MessageResponse.builder()
                .message("Add On Item deleted success").build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<AddOnItem> updateAddOnItemStatus(@PathVariable Long id,
                                                                 @RequestHeader("Authorization") String jwtToken) throws CustomException{
        AddOnItem updatedAddOnItem = addOnItemService.updateAddOnItemStatus(id);

        return new ResponseEntity<>(updatedAddOnItem, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddOnItem> getAddOnItemById(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwtToken) throws CustomException{
        AddOnItem addOnItem = addOnItemService.getAddOnItemById(id);
        return new ResponseEntity<>(addOnItem, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<AddOnItem>> getAddOnItemsByRestaurantId(@PathVariable Long restaurantId,
                                                                       @RequestHeader("Authorization") String jwtToken) throws CustomException {
        List<AddOnItem> addOnItems = addOnItemService.getAddOnItemByRestaurant(restaurantId);
        return new ResponseEntity<>(addOnItems, HttpStatus.OK);
    }
}
