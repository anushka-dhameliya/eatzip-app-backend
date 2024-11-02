package com.eatzip.service.address;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Address;
import com.eatzip.model.User;
import com.eatzip.request.CreateAddressRequest;

import java.util.List;

public interface AddressService {

    public Address createAddress(CreateAddressRequest addressRequest, User user) throws CustomException;

    public List<Address> getUsersAddresses(Long userId);
}
