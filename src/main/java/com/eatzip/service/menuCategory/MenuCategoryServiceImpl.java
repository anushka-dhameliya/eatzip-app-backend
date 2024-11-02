package com.eatzip.service.menuCategory;


import com.eatzip.exception.CustomException;
import com.eatzip.model.MenuCategory;
import com.eatzip.model.Restaurant;
import com.eatzip.repository.MenuCategoryRepository;
import com.eatzip.request.CreateMenuCategoryRequest;
import com.eatzip.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuCategoryServiceImpl implements MenuCategoryService {

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public MenuCategory createMenuCategory(CreateMenuCategoryRequest request) throws CustomException {
        try{
            Restaurant restaurant = restaurantService.findRestaurantById(request.getRestaurantId());
            MenuCategory menuCategory = MenuCategory.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .restaurant(restaurant)
                    .build();

            MenuCategory savedMenuCategory = menuCategoryRepository.save(menuCategory);
            restaurant.getMenuCategories().add(savedMenuCategory);
            return savedMenuCategory;
        }
        catch (Exception e){
            throw new CustomException("Error while creating menu category...");
        }
    }

    @Override
    public MenuCategory updateMenuCategory(Long id, CreateMenuCategoryRequest request) throws CustomException {
        MenuCategory menuCategoryDB = getMenuCategoryById(id);
        if(request.getName() != null){
            menuCategoryDB.setName(request.getName());
        }
        if(request.getDescription() != null){
            menuCategoryDB.setDescription(request.getDescription());
        }
        menuCategoryDB = menuCategoryRepository.save(menuCategoryDB);
        return menuCategoryDB;
    }

    @Override
    public void deleteMenuCategory(Long id) throws CustomException {
        MenuCategory menuCategoryDB = getMenuCategoryById(id);
        menuCategoryRepository.deleteById(menuCategoryDB.getId());
    }

    @Override
    public MenuCategory getMenuCategoryById(Long id) throws CustomException {
        Optional<MenuCategory> menuCategoryDB = menuCategoryRepository.findById(id);
        if(menuCategoryDB.isEmpty()){
            throw new CustomException("Menu Category not found with id = " + id);
        }
        return menuCategoryDB.get();
    }

    @Override
    public List<MenuCategory> getAllMenuCategoriesByRestaurant(Long restaurantId) throws CustomException {
        return menuCategoryRepository.findByRestaurant_IdOrderByIdAsc(restaurantId);
    }
}
