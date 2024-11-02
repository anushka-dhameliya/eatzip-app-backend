package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Address;
import com.eatzip.model.User;
import com.eatzip.request.CreateAddressRequest;
import com.eatzip.service.address.AddressService;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody CreateAddressRequest request,
                                                  @RequestHeader("Authorization") String jwt) throws CustomException {
        User user = userService.findUserByJwtToken(jwt);
        Address newAddress = addressService.createAddress(request, user);
        return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Address>> getUserAddresses(@RequestHeader("Authorization") String jwt) throws CustomException {
        User user = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(addressService.getUsersAddresses(user.getId()), HttpStatus.OK);
    }

}
