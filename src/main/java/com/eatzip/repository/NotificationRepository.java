package com.eatzip.repository;


import com.eatzip.model.Notification;
import com.eatzip.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_Id(Long id);

    List<Notification> getByUser_IdOrderByGeneratedTimeDesc(Long id);

    long countByUser_IdAndStatus(Long id, NotificationStatus status);


}