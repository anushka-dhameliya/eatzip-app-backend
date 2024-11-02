package com.eatzip.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {

    private String type;

    private String name;

    private String addressLine1;

    private String addressLine2;

    private String street;

    private String city;

    private String state;

    private String pinCode;
}
