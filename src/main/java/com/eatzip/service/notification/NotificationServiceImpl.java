package com.eatzip.service.notification;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Notification;
import com.eatzip.model.NotificationStatus;
import com.eatzip.model.Restaurant;
import com.eatzip.model.User;
import com.eatzip.repository.NotificationRepository;
import com.eatzip.request.CreateNotificationRequest;
import com.eatzip.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RestaurantService restaurantService;


    @Override
    public Notification createNotification(CreateNotificationRequest request, User user) throws CustomException {
        try{
            Notification notification = Notification.builder()
                    .description(request.getDescription())
                    .generatedTime(LocalDateTime.now())
                    .status(NotificationStatus.UNREAD)
                    .user(user)
                    .build();

            Notification savedNotification = notificationRepository.save(notification);
//            if(user.getNotifications().isEmpty())
//                user.setNotifications(new ArrayList<>());
//            user.getNotifications().add(savedNotification);
            return savedNotification;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new CustomException("Error while creating notification...");
        }
    }

    @Override
    public Notification updateNotificationStatus(Long notificationId, NotificationStatus status) throws CustomException {
        Notification notificationDB = getNotificationById(notificationId);
        if(status.equals(NotificationStatus.UNREAD)){
            notificationDB.setStatus(status);
            notificationDB.setReadTime(null);
        }
        else if(status.equals(NotificationStatus.READ)){
            notificationDB.setStatus(status);
            notificationDB.setReadTime(LocalDateTime.now());
        }

        Notification updatedNotification = notificationRepository.save(notificationDB);
        return updatedNotification;
    }

    @Override
    public Notification getNotificationById(Long notificationId) throws CustomException {
        Optional<Notification> notificationDB = notificationRepository.findById(notificationId);
        if(notificationDB.isEmpty()){
            throw new CustomException("Notification not found with id = " + notificationId);
        }
        return notificationDB.get();
    }

    @Override
    public List<Notification> getNotificationForUser(Long userId) {
        return notificationRepository.getByUser_IdOrderByGeneratedTimeDesc(userId);
    }

    @Override
    public List<Notification> getNotificationForUserByStatus(Long userId, NotificationStatus status){
        return getNotificationForUser(userId).stream().filter(i -> i.getStatus().equals(status)).toList();
    }

    @Override
    public void pushNotificationForCustomer(String message, User user) throws CustomException {
        CreateNotificationRequest request =  CreateNotificationRequest.builder()
                .description(message)
                .build();

        Notification notification = createNotification(request, user);
    }

    @Override
    public void pushNotificationForRestaurantOwner(String message, Restaurant restaurant) throws CustomException {
        User restaurantOwner = restaurantService.findRestaurantById(restaurant.getId()).getOwner();
        System.out.println("name : " + restaurantOwner.getFullName());
        CreateNotificationRequest request =  CreateNotificationRequest.builder()
                .description(message)
                .build();

        Notification notification = createNotification(request, restaurantOwner);
    }

    @Override
    public long getNotificationCount(Long userId) throws CustomException {
        return notificationRepository.countByUser_IdAndStatus(userId,NotificationStatus.UNREAD);
    }
}
