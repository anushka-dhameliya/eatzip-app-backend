package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.MenuCategory;
import com.eatzip.service.menuCategory.MenuCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menuCategory")
public class MenuCategoryController {

    @Autowired
    private MenuCategoryService menuCategoryService;

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
