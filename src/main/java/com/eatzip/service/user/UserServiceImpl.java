package com.eatzip.service.user;


import com.eatzip.config.JwtProvider;
import com.eatzip.exception.CustomException;
import com.eatzip.model.User;
import com.eatzip.repository.UserRepository;
import com.eatzip.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwtToken) throws CustomException {
        String email = jwtProvider.getEmailFromJwtToken(jwtToken);
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws CustomException {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new CustomException("User not found...");
        }

        return user;
    }

    @Override
    public User updateUserDetails(UserRequest userRequest) throws  CustomException{
        try{
            User user = findUserByEmail(userRequest.getEmail());

            if(userRequest.isUserLocked() != user.isUserLocked()){
                user.setUserLocked(userRequest.isUserLocked());
            }
            if(!userRequest.getFullName().equals(user.getFullName())){
                user.setFullName(userRequest.getFullName());
            }
            if(!userRequest.getPhoneNumber().equals(user.getPhoneNumber())){
                user.setPhoneNumber(userRequest.getPhoneNumber());
            }
            if(!userRequest.getProfileImageUrl().equals(user.getProfileImageUrl())){
                user.setProfileImageUrl(userRequest.getProfileImageUrl());
            }

            User updatedUser = userRepository.save(user);

            return updatedUser;
        }
        catch (Exception e) {
            throw new CustomException("Error while updating user details.");
        }
    }
}
