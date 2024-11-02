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
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @ManyToMany
    private List<AddOnItem> addOnItems = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private int quantity;

    private double totalAmount;
}
