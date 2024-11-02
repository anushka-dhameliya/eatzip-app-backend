package com.eatzip.service.notification;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Notification;
import com.eatzip.model.NotificationStatus;
import com.eatzip.model.Restaurant;
import com.eatzip.model.User;
import com.eatzip.request.CreateNotificationRequest;

import java.util.List;

public interface NotificationService {

    public Notification createNotification(CreateNotificationRequest request, User user) throws CustomException;

    public Notification updateNotificationStatus(Long notificationId, NotificationStatus status) throws  CustomException;

    public Notification getNotificationById(Long notificationId) throws CustomException;

    public List<Notification> getNotificationForUser(Long userId);

    public List<Notification> getNotificationForUserByStatus(Long userId, NotificationStatus status);

    public long getNotificationCount(Long userId) throws CustomException;

    public void pushNotificationForCustomer(String message, User user) throws CustomException;

    public void pushNotificationForRestaurantOwner(String message, Restaurant restaurant) throws CustomException;

}
