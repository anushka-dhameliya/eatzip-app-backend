package com.eatzip.model;

import com.eatzip.dto.RestaurantDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(length = 10)
    private String phoneNumber;

    private ROLE role;

    private boolean isUserLocked;

    private LocalDateTime creationDate;

    private LocalDateTime lastLoginDate;

    private String profileImageUrl;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private Cart cart;

    @ElementCollection
    private List<RestaurantDto> favorites = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();
}
