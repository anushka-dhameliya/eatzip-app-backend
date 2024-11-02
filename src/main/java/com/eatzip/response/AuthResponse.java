package com.eatzip.response;


import com.eatzip.model.ROLE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String jwtToken;

    private String message;

    private ROLE role;
}
