package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.User;
import com.eatzip.request.UserRequest;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserByJwtToken(@RequestHeader("Authorization") String jwtToken) throws CustomException {
        User user = userService.findUserByJwtToken(jwtToken);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUserDetails(
            @RequestBody UserRequest userRequest,
            @RequestHeader("Authorization") String jwtToken) throws CustomException{

        User updatedUser = userService.updateUserDetails(userRequest);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
