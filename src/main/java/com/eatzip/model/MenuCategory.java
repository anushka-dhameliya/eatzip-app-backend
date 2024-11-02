package com.eatzip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @JsonIgnore
    @OneToMany(mappedBy = "menuCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems;
}
