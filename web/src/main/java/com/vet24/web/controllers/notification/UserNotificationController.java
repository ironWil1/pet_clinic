package com.vet24.web.controllers.notification;


import com.vet24.models.dto.notification.UserNotificationDto;
import com.vet24.models.mappers.notification.NotificationMapper;
import com.vet24.models.mappers.notification.UserNotificationMapper;
import com.vet24.models.mappers.user.UserInfoMapper;
import com.vet24.models.notification.UserNotification;
import com.vet24.models.user.User;
import com.vet24.service.notification.UserNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user/notification")
public class UserNotificationController {

    private final UserNotificationService userNotificationService;
    private final UserNotificationMapper userNotificationMapper;
    private final NotificationMapper notificationMapper;
    private final UserInfoMapper userInfoMapper;

    public UserNotificationController(UserNotificationService userNotificationService,
                                      UserNotificationMapper userNotificationMapper, NotificationMapper notificationMapper, UserInfoMapper userInfoMapper) {
        this.userNotificationService = userNotificationService;
        this.userNotificationMapper = userNotificationMapper;
        this.notificationMapper = notificationMapper;
        this.userInfoMapper = userInfoMapper;
    }

    @Operation(summary = "Getting all notifications for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification list received"),
            @ApiResponse(responseCode = "404", description = "Notification list not found")
    })
    @GetMapping("")
    public ResponseEntity<List<UserNotificationDto>> getAllNotifications() {

        List<UserNotificationDto> userNotificationDtoList = new ArrayList<>();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserNotification> userNotificationList = userNotificationService.getAllUserNotificationByUserId(user.getId());

        for (UserNotification userNotification : userNotificationList) {
            UserNotificationDto userNotificationDto = userNotificationMapper.toDto(userNotification);
            userNotificationDto.setNotification(notificationMapper.toDto(userNotification.getNotification()));
            userNotificationDto.setUser(userInfoMapper.toDto(userNotification.getUser()));

            userNotificationDtoList.add(userNotificationDto);
        }

        return new ResponseEntity<>(userNotificationDtoList, HttpStatus.OK);
    }

    @Operation(summary = "Receive notification by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification received"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @GetMapping("/{notificationId}")
    public ResponseEntity<UserNotificationDto> getUserNotificationById(@PathVariable("notificationId") Long notificationId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserNotification userNotification = userNotificationService.getByKey(notificationId);


        if (userNotification.getUser().getId() == user.getId()) {
            UserNotificationDto userNotificationDto = userNotificationMapper.toDto(userNotification);
            userNotificationDto.setNotification(notificationMapper.toDto(userNotification.getNotification()));
            userNotificationDto.setUser(userInfoMapper.toDto(userNotification.getUser()));
            return new ResponseEntity<>(userNotificationDto, HttpStatus.OK);
        } else {
            log.info("Notification is not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{notificationId}")
    public void notificationsStatus(@PathVariable("notificationId") Long notificationId) {

        UserNotification userNotification = userNotificationService.getByKey(notificationId);
        userNotification.setShow(false);
        userNotificationService.update(userNotification);

    }
}
