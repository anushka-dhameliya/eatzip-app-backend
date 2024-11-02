package com.eatzip.service.user;


import com.eatzip.exception.CustomException;
import com.eatzip.model.User;
import com.eatzip.request.UserRequest;

public interface UserService {

    public User findUserByJwtToken(String jwtToken) throws CustomException;

    public User findUserByEmail(String email) throws CustomException;

    public User updateUserDetails(UserRequest userRequest) throws  CustomException;
}
