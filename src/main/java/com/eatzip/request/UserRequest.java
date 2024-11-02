package com.eatzip.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserRequest {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private boolean isUserLocked;

    private LocalDateTime lastLoginDate;

    private String profileImageUrl;
}
