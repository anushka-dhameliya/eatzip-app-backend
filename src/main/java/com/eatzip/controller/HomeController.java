package com.eatzip.controller;

import com.eatzip.exception.CustomException;
import com.eatzip.model.*;
import com.eatzip.service.addOnItem.AddOnItemService;
import com.eatzip.service.menuCategory.MenuCategoryService;
import com.eatzip.service.menuItem.MenuItemService;
import com.eatzip.service.offer.OfferService;
import com.eatzip.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<String> getHome(){
        return new ResponseEntity<>("Welcome to EatZip web application.", HttpStatus.OK);
    }


    /***    Restaurant Home Service     ***/
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants(){
        return new ResponseEntity<>(restaurantService.getAllRestaurants(), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long restaurantId) throws Exception{
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


    /***    Add-On Home Service     ***/
    @Autowired
    private AddOnItemService addOnItemService;

    @GetMapping("/addOnItems/{id}")
    public ResponseEntity<AddOnItem> getAddOnItemById(@PathVariable Long id) throws CustomException{
        AddOnItem addOnItem = addOnItemService.getAddOnItemById(id);
        return new ResponseEntity<>(addOnItem, HttpStatus.OK);
    }

    @GetMapping("/addOnItems/restaurant/{restaurantId}")
    public ResponseEntity<List<AddOnItem>> getAddOnItemsByRestaurantId(@PathVariable Long restaurantId) throws CustomException{
        List<AddOnItem> addOnItems = addOnItemService.getAddOnItemByRestaurant(restaurantId);
        return new ResponseEntity<>(addOnItems, HttpStatus.OK);
    }



    /***    Menu-Category Home Service  ***/
    @Autowired
    private MenuCategoryService menuCategoryService;

    @GetMapping("/menuCategory/{id}")
    public ResponseEntity<MenuCategory> getMenuCategoryById(@PathVariable Long id) throws CustomException{
        MenuCategory menuCategory = menuCategoryService.getMenuCategoryById(id);
        return new ResponseEntity<>(menuCategory, HttpStatus.OK);
    }

    @GetMapping("/menuCategory/restaurant/{id}")
    public ResponseEntity<List<MenuCategory>> getAllMenuCategoriesByRestaurant(@PathVariable Long id) throws CustomException{
        List<MenuCategory> menuCategories = menuCategoryService.getAllMenuCategoriesByRestaurant(id);
        return new ResponseEntity<>(menuCategories, HttpStatus.OK);
    }



    /***    Menu Item Home Service  ***/
    @Autowired
    private MenuItemService menuItemService;


    @GetMapping("/restaurant/{restaurantId}/menu")
    public ResponseEntity<Map<String,List<MenuItem>>> getRestaurantMenus(@PathVariable Long restaurantId) throws CustomException{
        return new ResponseEntity<>(menuItemService.getRestaurantMenu(restaurantId), HttpStatus.OK);
    }

    @GetMapping("/menuItems/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) throws CustomException{
        MenuItem menuItem = menuItemService.findMenuItemById(id);

        return new ResponseEntity<>(menuItem, HttpStatus.OK);
    }

    @GetMapping("/menuItems/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long restaurantId) throws CustomException{
        List<MenuItem> menuItems = menuItemService.getAllMenuItemsByRestaurant(restaurantId);

        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping("/menuItems/filter")
    public ResponseEntity<List<MenuItem>> filterMenuItemsByRestaurant(@RequestParam(name = "restaurantId", required = false) Long restaurantId,
                                                                      @RequestParam(name = "name", required = false) String name,
                                                                      @RequestParam(name = "isVegetarian", required = false) String isVegetarian,
                                                                      @RequestParam(name = "category", required = false) String category) throws CustomException{
        if(restaurantId != 0L){
            List<MenuItem> menuItems = menuItemService.filterMenuItemsByRestaurant(restaurantId, name, isVegetarian, category);
            return new ResponseEntity<>(menuItems, HttpStatus.OK);
        }

        List<MenuItem> menuItems = menuItemService.filterMenuItems(name, isVegetarian, category);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }


    /***    Offer Home Service  ***/
    @Autowired
    private OfferService offerService;

    @GetMapping("/offers/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id) throws CustomException{
        Offer offer = offerService.getOfferById(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @GetMapping("/offers/restaurant/{restaurantId}")
    public ResponseEntity<List<Offer>> getOffersForRestaurant(@PathVariable Long restaurantId) throws CustomException {
        List<Offer> offers = offerService.getAllOffersForRestaurant(restaurantId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}
