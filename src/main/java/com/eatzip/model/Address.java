package com.eatzip.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String type;
    
    private String name;

    private String addressLine1;

    private String addressLine2;

    private String street;

    private String city;

    private String state;

    @Column(length = 6)
    private String pinCode;
}
