package com.eatzip.controller;


import com.eatzip.config.JwtProvider;
import com.eatzip.exception.CustomException;
import com.eatzip.model.Cart;
import com.eatzip.model.ROLE;
import com.eatzip.model.User;
import com.eatzip.repository.CartRepository;
import com.eatzip.repository.UserRepository;
import com.eatzip.request.LoginRequest;
import com.eatzip.response.AuthResponse;
import com.eatzip.service.notification.NotificationMessage;
import com.eatzip.service.notification.NotificationService;
import com.eatzip.service.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws CustomException, Exception {

        User existingUser = userRepository.findByEmail(user.getEmail());

        if(existingUser != null){
            throw  new CustomException("User already with given email.");
        }

        User newUser = User.builder()
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(user.getRole())
                .isUserLocked(false)
                .creationDate(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(newUser);

        if(savedUser.getRole().equals(ROLE.ROLE_CUSTOMER)){
            Cart cart = Cart.builder().customer(savedUser).build();
            Cart savedCart = cartRepository.save(cart);
            String notificationMessage = NotificationMessage.USER_CREATION.message;
            notificationService.pushNotificationForCustomer(notificationMessage, savedUser);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = AuthResponse.builder()
                .jwtToken(jwtToken)
                .message("Register User Success")
                .role(savedUser.getRole())
                .build();


        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest request) throws CustomException{
        String email = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(email, password);

        User savedUser = userRepository.findByEmail(email);
        savedUser.setLastLoginDate(LocalDateTime.now());
        userRepository.save(savedUser);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = AuthResponse.builder()
                .jwtToken(jwt)
                .role(ROLE.valueOf(role))
                .message("Login Success")
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String email, String password) throws CustomException{

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if(userDetails == null){
            throw new CustomException("Invalid Email...");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new CustomException("Invalid Password...");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
