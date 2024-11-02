package com.eatzip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private LocalDateTime generatedTime;

    private LocalDateTime readTime;

    private NotificationStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
