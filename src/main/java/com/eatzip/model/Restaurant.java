package com.eatzip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private User owner;

    private boolean isOpen;

    private String workingHours;

    private boolean isVegetarian;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    private LocalDateTime registrationDate;

    @Embedded
    private ContactInformation contactInformation;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "restaurant")
    private List<MenuCategory> menuCategories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "restaurant")
    private List<MenuItem> menuItems = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "restaurant")
    private List<Offer> offers = new ArrayList<>();

    @Column(length = 1000)
    @ElementCollection
    private List<String> images;
}
