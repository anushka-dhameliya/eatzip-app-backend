package com.eatzip.service.addOnItem;


import com.eatzip.exception.CustomException;
import com.eatzip.model.AddOnItem;
import com.eatzip.model.Restaurant;
import com.eatzip.repository.AddOnItemRepository;
import com.eatzip.request.CreateAddOnItemRequest;
import com.eatzip.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddOnItemServiceImpl implements AddOnItemService{

    @Autowired
    private AddOnItemRepository addOnItemRepository;

    @Autowired
    private RestaurantService restaurantService;


    @Override
    public AddOnItem createAddOnItem(CreateAddOnItemRequest request, Restaurant restaurant) throws CustomException {
        try {
            AddOnItem addOnItem = AddOnItem.builder()
                    .name(request.getName())
                    .price(request.getPrice())
                    .inStock(request.isInStock())
                    .restaurant(restaurant)
                    .menuItems(request.getMenuItems())
                    .build();

            AddOnItem savedAddOneItem = addOnItemRepository.save(addOnItem);

            return savedAddOneItem;
        }
        catch (Exception e){
            throw new CustomException("Error while creating Add On Item for Restaurant with id = " + restaurant.getId());
        }
    }

    @Override
    public AddOnItem updateAddOnItem(Long addOnItemId, CreateAddOnItemRequest request) throws CustomException {
        AddOnItem addOnItemDB = getAddOnItemById(addOnItemId);

        if(request.getName() != null){
            addOnItemDB.setName(request.getName());
        }
        if(request.getPrice() != addOnItemDB.getPrice()){
            addOnItemDB.setPrice(request.getPrice());
        }
        if(request.isInStock() != addOnItemDB.isInStock()){
            addOnItemDB.setInStock(request.isInStock());
        }
        if(request.getMenuItems() != null){
            addOnItemDB.setMenuItems(request.getMenuItems());
        }

        AddOnItem updatedAddOnItem = addOnItemRepository.save(addOnItemDB);

        return updatedAddOnItem;
    }

    @Override
    public void deleteAddOnItem(Long addOnItemId) throws CustomException {

        AddOnItem addOnItemDB = getAddOnItemById(addOnItemId);
        addOnItemRepository.deleteById(addOnItemDB.getId());
    }

    @Override
    public AddOnItem updateAddOnItemStatus(Long addOnItemId) throws CustomException {
        AddOnItem addOnItemDB = getAddOnItemById(addOnItemId);
        addOnItemDB.setInStock(!addOnItemDB.isInStock());
        return addOnItemRepository.save(addOnItemDB);
    }

    @Override
    public AddOnItem getAddOnItemById(Long addOnItemId) throws CustomException{
        Optional<AddOnItem> addOnItemDB = addOnItemRepository.findById(addOnItemId);
        if(addOnItemDB.isEmpty()){
            throw new CustomException("Add On Item is not found with id = " + addOnItemId);
        }
        return addOnItemDB.get();
    }

    @Override
    public List<AddOnItem> getAddOnItemByRestaurant(Long restaurantId) throws CustomException{
        return addOnItemRepository.findByRestaurant_Id(restaurantId);
    }
}
