package com.eatzip.service.menuCategory;


import com.eatzip.exception.CustomException;
import com.eatzip.model.MenuCategory;
import com.eatzip.request.CreateMenuCategoryRequest;

import java.util.List;

public interface MenuCategoryService {

    public MenuCategory createMenuCategory(CreateMenuCategoryRequest request) throws CustomException;

    public MenuCategory updateMenuCategory(Long id, CreateMenuCategoryRequest request) throws CustomException;

    public void deleteMenuCategory(Long id) throws CustomException;

    public MenuCategory getMenuCategoryById(Long id) throws CustomException;

    public List<MenuCategory> getAllMenuCategoriesByRestaurant(Long restaurantId) throws CustomException;
}
