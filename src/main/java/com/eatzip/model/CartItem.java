package com.eatzip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @ManyToMany
    private List<AddOnItem> addOnItems = new ArrayList<>();

    private int quantity;

    private double totalAmount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

}
