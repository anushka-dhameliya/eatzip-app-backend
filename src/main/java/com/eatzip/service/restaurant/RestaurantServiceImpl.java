package com.eatzip.service.restaurant;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Address;
import com.eatzip.model.Restaurant;
import com.eatzip.model.User;
import com.eatzip.repository.AddressRepository;
import com.eatzip.repository.RestaurantRepository;
import com.eatzip.repository.UserRepository;
import com.eatzip.request.CreateRestaurantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest request, User user) throws CustomException {
        try{
            Address address = addressRepository.save(request.getAddress());

            Restaurant restaurant = Restaurant.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .address(address)
                    .owner(user)
                    .isOpen(request.isOpen())
                    .images(request.getImages())
                    .isVegetarian(request.isVegetarian())
                    .workingHours(request.getWorkingHours())
                    .contactInformation(request.getContactInformation())
                    .registrationDate(LocalDateTime.now())
                    .build();

            Restaurant savedRestaurant = restaurantRepository.save(restaurant);

            return savedRestaurant;
        }
        catch (Exception e){
            throw new CustomException("Error while creating a restaurant...");
        }
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest request) throws CustomException {

        Restaurant restaurantDB = findRestaurantById(restaurantId);

        if(request.getName() != null){
            restaurantDB.setName(request.getName());
        }
        if(request.getDescription() != null){
            restaurantDB.setDescription(request.getDescription());
        }
        if(!request.getImages().isEmpty()){
            restaurantDB.setImages(request.getImages());
        }
        if(request.getAddress() != null){
            Address addressDB = addressRepository.save(request.getAddress());
            restaurantDB.setAddress(addressDB);
        }
        if(request.getContactInformation() != null){
            restaurantDB.setContactInformation(request.getContactInformation());
        }
        if(request.getWorkingHours() != null){
            restaurantDB.setWorkingHours(request.getWorkingHours());
        }
        restaurantDB.setVegetarian(request.isVegetarian());

        restaurantDB = restaurantRepository.save(restaurantDB);

        return restaurantDB;
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws CustomException {

        Restaurant restaurantDB = findRestaurantById(restaurantId);

        restaurantRepository.deleteById(restaurantId);
    }

    @Override
    public Restaurant findRestaurantById(Long restaurantId) throws CustomException {

        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);

        if(restaurant.isPresent()){
            return restaurant.get();
        }

        throw new CustomException("Restaurant does not exist with given id: " + restaurantId);
    }

    @Override
    public Restaurant findRestaurantByUserId(Long userId) throws CustomException {
        Optional<Restaurant> restaurant = restaurantRepository.findByOwnerId(userId);
        if(restaurant.isEmpty()){
            throw new CustomException("Restaurant do not have owner with owner id:" + userId);
        }
        return restaurant.get();
    }

    @Override
    public Restaurant updateRestaurantStatus(Long restaurantId) throws CustomException {

        Restaurant restaurantDB = findRestaurantById(restaurantId);

        restaurantDB.setOpen(!restaurantDB.isOpen());
        restaurantDB = restaurantRepository.save(restaurantDB);

        return restaurantDB;
    }





    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    /*@Override
    public List<Restaurant> searchRestaurantsBasedOnFilter(RestaurantFilter filter) {
        return restaurantRepository.searchByFilter(filter.getName(),
                filter.getIsVegetarian(),
                filter.getCity(),
                filter.getPinCode(),
                filter.getMenuCategory(),
                filter.isOpen());
    }*/

    @Override
    public List<Restaurant> searchRestaurantsBasedOnFilter(String name, String isOpen, String isVegetarian, String city, String pinCode, String category) {
        List<Restaurant> restaurants = getAllRestaurants();
        Set<Restaurant> filteredRestaurants = new HashSet<>();

        if(name != null && !name.isEmpty()){
            filteredRestaurants = restaurants.stream().filter(i -> i.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet());
        }
        if(category != null && !category.isEmpty()){
            for(Restaurant restaurant : restaurants){
                if(restaurant.getMenuCategories() != null && !restaurant.getMenuCategories().isEmpty()){
                    boolean isPresent = restaurant.getMenuCategories().stream().anyMatch(i -> i.getName().toLowerCase().contains(category.toLowerCase()));
                    if(isPresent)
                        filteredRestaurants.add(restaurant);
                }
            }
        }
        if(city != null && !city.isEmpty()){
            for(Restaurant restaurant : restaurants){
                if(restaurant.getAddress() != null){
                    if(restaurant.getAddress().getCity() != null && restaurant.getAddress().getCity().equalsIgnoreCase(city)){
                        filteredRestaurants.add(restaurant);
                    }
                }
            }
        }

        if(pinCode != null && !pinCode.isEmpty()){
            for(Restaurant restaurant : restaurants){
                if(restaurant.getAddress() != null){
                    if(restaurant.getAddress().getPinCode() != null && restaurant.getAddress().getPinCode().equalsIgnoreCase(pinCode)){
                        filteredRestaurants.add(restaurant);
                    }
                }
            }
        }

        if(isOpen != null && !isOpen.isEmpty()){
            if(isOpen.equals("true")){
                filteredRestaurants.addAll(restaurants.stream().filter(i -> i.isOpen()).collect(Collectors.toSet()));
            }
            if(isOpen.equals("false")){
                filteredRestaurants.addAll(restaurants.stream().filter(i -> !i.isOpen()).collect(Collectors.toSet()));
            }
        }

        if(isVegetarian != null && !isVegetarian.isEmpty()){
            if(isVegetarian.equals("true")){
                filteredRestaurants.addAll(restaurants.stream().filter(i -> i.isVegetarian()).collect(Collectors.toSet()));
            }
            if(isVegetarian.equals("false")){
                filteredRestaurants.addAll(restaurants.stream().filter(i -> !i.isVegetarian()).collect(Collectors.toSet()));
            }
        }

        return filteredRestaurants.stream().toList();
    }

}
