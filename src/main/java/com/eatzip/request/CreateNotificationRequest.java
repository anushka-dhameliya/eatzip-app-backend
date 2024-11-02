package com.eatzip.request;

import com.eatzip.model.NotificationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateNotificationRequest {

    private String description;

    private LocalDateTime generatedTime;

    private LocalDateTime readTime;

    private NotificationStatus status;

    private Long userId;
}
