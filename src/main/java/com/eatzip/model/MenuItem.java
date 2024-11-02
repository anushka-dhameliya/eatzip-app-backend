package com.eatzip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private boolean isVegetarian;

    private double price;

    @JsonIgnore
    @ManyToMany(mappedBy = "menuItems")
    private List<AddOnItem> addOnItems = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "menu_category_id", nullable = false)
    private MenuCategory menuCategory;

    @Column(length = 1000)
    @ElementCollection
    private List<String> images;
}
