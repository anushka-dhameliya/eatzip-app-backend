package com.eatzip.service.addOnItem;


import com.eatzip.exception.CustomException;
import com.eatzip.model.AddOnItem;
import com.eatzip.model.Restaurant;
import com.eatzip.request.CreateAddOnItemRequest;

import java.util.List;

public interface AddOnItemService {

    public AddOnItem createAddOnItem(CreateAddOnItemRequest request, Restaurant restaurant) throws CustomException;

    public AddOnItem updateAddOnItem(Long addOnItemId, CreateAddOnItemRequest request) throws CustomException;

    public void deleteAddOnItem(Long addOnItemId) throws CustomException;

    public AddOnItem updateAddOnItemStatus(Long addOnItemId) throws CustomException;

    public AddOnItem getAddOnItemById(Long addOnItemId) throws CustomException;

    public List<AddOnItem> getAddOnItemByRestaurant(Long restaurantId) throws CustomException;
}
