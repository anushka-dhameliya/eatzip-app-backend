package com.eatzip.request;


import com.eatzip.model.MenuItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateAddOnItemRequest {

    private String name;

    private double price;

    private boolean inStock;

    private List<MenuItem> menuItems;

    private Long restaurantId;
}
