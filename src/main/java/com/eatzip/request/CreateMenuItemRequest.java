package com.eatzip.request;

import com.eatzip.model.AddOnItem;
import com.eatzip.model.MenuCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateMenuItemRequest {

    private String name;

    private String description;

    private MenuCategory menuCategory;

    private boolean isVegetarian;

    private double price;

    private Long restaurantId;

    private List<String> images;

    private List<AddOnItem> addOnItems;
}
