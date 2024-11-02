package com.eatzip.request;


import com.eatzip.model.MenuItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateMenuCategoryRequest {

    private String name;
    private String description;
    private Long restaurantId;
    private List<MenuItem> menuItems;
}
