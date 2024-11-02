package com.eatzip.service.address;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Address;
import com.eatzip.model.User;
import com.eatzip.repository.AddressRepository;
import com.eatzip.repository.UserRepository;
import com.eatzip.request.CreateAddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements  AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Address createAddress(CreateAddressRequest addressRequest, User user) throws CustomException {
        Address address = Address.builder()
                .type(addressRequest.getType())
                .name(addressRequest.getName())
                .addressLine1(addressRequest.getAddressLine1())
                .addressLine2(addressRequest.getAddressLine2())
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .pinCode(addressRequest.getPinCode())
                .build();

        Address newAddress = addressRepository.save(address);
        if(user.getAddresses().isEmpty()){
            user.setAddresses(new ArrayList<>());
        }
        user.getAddresses().add(newAddress);
        userRepository.save(user);

        return newAddress;
    }

    @Override
    public List<Address> getUsersAddresses(Long userId) {
        return userRepository.findById(userId).get().getAddresses();
    }
}
