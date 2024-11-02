package com.eatzip.request;


import com.eatzip.model.AddOnItem;
import com.eatzip.model.MenuItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddCartItemRequest {

    private MenuItem menuItem;

    private List<AddOnItem> addOnItems;

    private int quantity;

    private double totalAmount;
}
