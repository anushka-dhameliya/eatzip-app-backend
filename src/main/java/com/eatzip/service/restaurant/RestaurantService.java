package com.eatzip.service.restaurant;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Restaurant;
import com.eatzip.model.User;
import com.eatzip.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest request, User user) throws CustomException;

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest request) throws  CustomException;

    public void deleteRestaurant(Long restaurantId) throws CustomException;

    public Restaurant findRestaurantById(Long restaurantId) throws CustomException;

    public Restaurant findRestaurantByUserId(Long userId) throws CustomException;

    //public RestaurantDto addToFavorites(Long restaurantId, User user) throws  CustomException;

    public Restaurant updateRestaurantStatus(Long restaurantId) throws CustomException;



    public List<Restaurant> getAllRestaurants();

    public List<Restaurant> searchRestaurantsBasedOnFilter(String name, String isOpen, String isVegetarian, String city, String pinCode, String category);
}
