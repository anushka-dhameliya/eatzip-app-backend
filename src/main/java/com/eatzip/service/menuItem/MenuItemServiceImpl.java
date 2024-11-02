package com.eatzip.service.menuItem;


import com.eatzip.exception.CustomException;
import com.eatzip.model.MenuCategory;
import com.eatzip.model.MenuItem;
import com.eatzip.model.Restaurant;
import com.eatzip.repository.MenuItemRepository;
import com.eatzip.request.CreateMenuItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuItemServiceImpl implements MenuItemService{

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public MenuItem createMenuItem(CreateMenuItemRequest request, MenuCategory category, Restaurant restaurant) throws CustomException {
        try{
            MenuItem menuItem = MenuItem.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .isVegetarian(request.isVegetarian())
                    .images(request.getImages())
                    .menuCategory(category)
                    .restaurant(restaurant)
                    .addOnItems(request.getAddOnItems())
                    .price(request.getPrice())
                    .build();

            MenuItem savedMenuItem = menuItemRepository.save(menuItem);
            restaurant.getMenuItems().add(savedMenuItem);

            return savedMenuItem;
        }
        catch (Exception e){
            throw new CustomException("Error while creating Menu Item for a Restaurant...");
        }
    }

    public MenuItem updateMenuItem(Long menuItemId, CreateMenuItemRequest request) throws CustomException{
        MenuItem menuItemDB = findMenuItemById(menuItemId);

        if(request.getName() != null){
            menuItemDB.setName(request.getName());
        }
        if(request.getDescription() != null){
            menuItemDB.setDescription(request.getDescription());
        }
        if(request.getAddOnItems() != null){
            menuItemDB.setAddOnItems(request.getAddOnItems());
        }
        if(request.getImages() != null){
            menuItemDB.setImages(request.getImages());
        }
        if(request.getMenuCategory() != null){
            menuItemDB.setMenuCategory(request.getMenuCategory());
        }
        if(request.getPrice() != menuItemDB.getPrice()){
            menuItemDB.setPrice(request.getPrice());
        }
        if(request.isVegetarian() != menuItemDB.isVegetarian()){
            menuItemDB.setVegetarian(request.isVegetarian());
        }

        Restaurant restaurant = menuItemDB.getRestaurant();
        restaurant.getMenuItems().remove(menuItemDB);
        MenuItem savedMenuItem = menuItemRepository.save(menuItemDB);
        restaurant.getMenuItems().add(savedMenuItem);

        return savedMenuItem;


    }

    @Override
    public void deleteMenuItem(Long menuItemId) throws CustomException {
        MenuItem menuItemDB = findMenuItemById(menuItemId);
        Restaurant restaurant = menuItemDB.getRestaurant();
        restaurant.getMenuItems().remove(menuItemDB);

        menuItemRepository.deleteById(menuItemId);
    }

    @Override
    public List<MenuItem> getAllMenuItemsByRestaurant(Long restaurantId) throws CustomException {
        return menuItemRepository.findByRestaurant_IdOrderByMenuCategory_IdAsc(restaurantId);
    }

    @Override
    public List<MenuItem> filterMenuItemsByRestaurant(Long restaurantId, String name, String isVegetarian, String category) throws CustomException {
        List<MenuItem> menuItems = getAllMenuItemsByRestaurant(restaurantId);
        if(name != null && !name.isEmpty()){
            menuItems = menuItems.stream().filter(m -> m.getName().contains(name)).toList();
        }
        if(isVegetarian != null && !isVegetarian.isEmpty()) {
            if(isVegetarian.equalsIgnoreCase("veg")){
                menuItems = menuItems.stream().filter(MenuItem::isVegetarian).toList();
            }
            else if(isVegetarian.equalsIgnoreCase("non-veg")){
                menuItems = menuItems.stream().filter(m -> !m.isVegetarian()).toList();
            }
        }
        if(category != null){
            menuItems = menuItems.stream().filter(m -> m.getMenuCategory().getName().contains(category)).toList();
        }

        return menuItems;
    }

    public List<MenuItem> filterMenuItems(String name, String isVegetarian, String category) throws CustomException{
        if(isVegetarian != null && !isVegetarian.isEmpty()) {
            if(isVegetarian.equalsIgnoreCase("veg")){
                return menuItemRepository.findByNameLikeIgnoreCaseOrIsVegetarianOrMenuCategory_NameLikeIgnoreCase(name, true, category);
            }
            else if(isVegetarian.equalsIgnoreCase("non-veg")){
                return menuItemRepository.findByNameLikeIgnoreCaseOrIsVegetarianOrMenuCategory_NameLikeIgnoreCase(name, false, category);
            }
        }
        return menuItemRepository.findByNameLikeIgnoreCaseOrIsVegetarianOrMenuCategory_NameLikeIgnoreCase(name, false, category);
    }

    @Override
    public MenuItem findMenuItemById(Long menuItemId) throws CustomException {
        Optional<MenuItem> menuItem = menuItemRepository.findById(menuItemId);

        if(menuItem.isEmpty()){
            throw new CustomException("Menu Item not found with id = " + menuItemId);
        }

        return menuItem.get();
    }

    @Override
    public Map<String, List<MenuItem>> getRestaurantMenu(Long restaurantId) throws CustomException {
        List<MenuItem> menuItems = getAllMenuItemsByRestaurant(restaurantId);
        Map<String, List<MenuItem>> menu = new HashMap<>();

        for(MenuItem item : menuItems){
            if(menu.containsKey(item.getMenuCategory().getName())){
                menu.get(item.getMenuCategory().getName()).add(item);
            }
            else{
                menu.put(item.getMenuCategory().getName(), new ArrayList<>());
                menu.get(item.getMenuCategory().getName()).add(item);
            }
        }

        return menu;
    }
}
