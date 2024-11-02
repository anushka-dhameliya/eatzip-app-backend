package com.eatzip.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class RestaurantDto {

    private Long id;

    private String name;

    private String description;

    private boolean isOpen;

    private String workingHours;

    private boolean isVegetarian;

    @Column(length = 1000)
    private List<String> images;
}
