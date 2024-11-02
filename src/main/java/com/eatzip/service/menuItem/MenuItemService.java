package com.eatzip.service.menuItem;


import com.eatzip.exception.CustomException;
import com.eatzip.model.MenuCategory;
import com.eatzip.model.MenuItem;
import com.eatzip.model.Restaurant;
import com.eatzip.request.CreateMenuItemRequest;

import java.util.List;
import java.util.Map;

public interface MenuItemService {

    public MenuItem createMenuItem(CreateMenuItemRequest request, MenuCategory category, Restaurant restaurant) throws CustomException;

    public MenuItem updateMenuItem(Long menuItemId, CreateMenuItemRequest request) throws CustomException;

    void deleteMenuItem(Long menuItemId) throws CustomException;

    public List<MenuItem> getAllMenuItemsByRestaurant(Long restaurantId) throws CustomException;

    public List<MenuItem> filterMenuItemsByRestaurant(Long restaurantId, String name, String isVegetarian, String category) throws CustomException;

    public List<MenuItem> filterMenuItems(String name, String isVegetarian, String category) throws CustomException;

    public MenuItem findMenuItemById(Long menuItemId) throws CustomException;

    public Map<String, List<MenuItem>> getRestaurantMenu(Long restaurantId) throws CustomException;
}
