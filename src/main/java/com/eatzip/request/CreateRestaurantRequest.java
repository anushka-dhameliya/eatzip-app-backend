package com.eatzip.request;


import com.eatzip.model.Address;
import com.eatzip.model.ContactInformation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRestaurantRequest {

    private Long id;
    private String name;
    private String description;
    private boolean isOpen;
    private String workingHours;
    private boolean isVegetarian;
    private Address address;
    private ContactInformation contactInformation;
    private List<String> images;
}
