package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Notification;
import com.eatzip.model.NotificationStatus;
import com.eatzip.model.User;
import com.eatzip.service.notification.NotificationService;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotification(@RequestHeader("Authorization") String jwtToken) throws Exception{
        User user = userService.findUserByJwtToken(jwtToken);
        List<Notification> notifications = notificationService.getNotificationForUser(user.getId());
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Notification>> getAllNotificationsByStatus(@RequestParam("status") NotificationStatus status,
                                                                          @RequestHeader("Authorization") String jwtToken) throws CustomException {
        User user = userService.findUserByJwtToken(jwtToken);
        List<Notification> notifications = notificationService.getNotificationForUserByStatus(user.getId(), status);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getNotificationCount(@RequestHeader("Authorization") String jwtToken) throws CustomException{
        User user = userService.findUserByJwtToken(jwtToken);
        long count = notificationService.getNotificationCount(user.getId());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id,  @RequestHeader("Authorization") String jwtToken) throws CustomException{
        Notification notification = notificationService.getNotificationById(id);
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotificationStatus(@PathVariable Long id,
                                                           @RequestParam(name = "status") String status,
                                                           @RequestHeader("Authorization") String jwtToken) throws CustomException {
        Notification notification = notificationService.updateNotificationStatus(id, NotificationStatus.valueOf(status));
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

    /*@PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody CreateNotificationRequest request,
                                                           @RequestHeader("Authorization") String jwtToken) throws CustomException{
        User user = userService.findUserByJwtToken(jwtToken);
        Notification notification = notificationService.createNotification(request, user);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }*/
}
