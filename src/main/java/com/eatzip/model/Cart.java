package com.eatzip.model;

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
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cart")
    List<CartItem> cartItems = new ArrayList<>();

    private int totalItems;

    @OneToOne
    private User customer;
}
