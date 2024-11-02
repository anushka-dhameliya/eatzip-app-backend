package com.eatzip.controller.restaurantOwner;


import com.eatzip.exception.CustomException;
import com.eatzip.model.MenuCategory;
import com.eatzip.request.CreateMenuCategoryRequest;
import com.eatzip.response.MessageResponse;
import com.eatzip.service.menuCategory.MenuCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menuCategory")
public class MenuCategoryOwnerController {

    @Autowired
    private MenuCategoryService menuCategoryService;

    @PostMapping
    public ResponseEntity<MenuCategory> createMenuCategory(@RequestBody CreateMenuCategoryRequest request,
                                                           @RequestHeader("Authorization") String jwtToken) throws CustomException {
        MenuCategory menuCategory = menuCategoryService.createMenuCategory(request);
        return new ResponseEntity<>(menuCategory, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuCategory> updateMenuCategory(@PathVariable Long id,
                                                           @RequestBody CreateMenuCategoryRequest request,
                                                           @RequestHeader("Authorization") String jwtToken) throws CustomException{
        MenuCategory menuCategory = menuCategoryService.updateMenuCategory(id, request);
        return new ResponseEntity<>(menuCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteMenuCategory(@PathVariable Long id,
                                                              @RequestHeader("Authorization") String jwtToken) throws CustomException{
        menuCategoryService.deleteMenuCategory(id);
        MessageResponse messageResponse = MessageResponse.builder()
                .message("Menu Category delete success").build();

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuCategory> getMenuCategoryById(@PathVariable Long id,
                                                           @RequestHeader("Authorization") String jwtToken) throws CustomException{
        MenuCategory menuCategory = menuCategoryService.getMenuCategoryById(id);
        return new ResponseEntity<>(menuCategory, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<MenuCategory>> getAllMenuCategoriesByRestaurant(@PathVariable Long id,
                                                                 @RequestHeader("Authorization") String jwtToken) throws CustomException {
        List<MenuCategory> menuCategories = menuCategoryService.getAllMenuCategoriesByRestaurant(id);
        return new ResponseEntity<>(menuCategories, HttpStatus.OK);
    }
}
