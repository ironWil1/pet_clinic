package com.vet24.web.controllers.notification;

import com.vet24.models.dto.notification.UserNotificationDto;
import com.vet24.models.mappers.dto.UserNotificationDtoMapper;
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
import org.webjars.NotFoundException;

import java.util.List;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;


@RestController
@Slf4j
@RequestMapping("/api/user/notification")
public class UserNotificationController {

    private final UserNotificationService userNotificationService;
    private final UserNotificationDtoMapper userNotificationDtoMapper;

    public UserNotificationController(UserNotificationService userNotificationService, UserNotificationDtoMapper userNotificationDtoMapper) {
        this.userNotificationService = userNotificationService;
        this.userNotificationDtoMapper = userNotificationDtoMapper;
    }

    @Operation(summary = "Getting all notifications for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification list received"),
            @ApiResponse(responseCode = "404", description = "Notification list not found")
    })
    @GetMapping("")
    public ResponseEntity<List<UserNotificationDto>> getAllNotifications() {

//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) getSecurityUserOrNull();
        List<UserNotification> userNotificationList = userNotificationService.getAllUserNotificationByUserId(user.getId());

        return new ResponseEntity<>(userNotificationDtoMapper.toDto(userNotificationList), HttpStatus.OK);
    }

    @Operation(summary = "Receive notification by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification received"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @GetMapping("/{notificationId}")
    public ResponseEntity<UserNotificationDto> getUserNotificationById(@PathVariable("notificationId") Long notificationId) {

//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) getSecurityUserOrNull();
        UserNotification userNotification = userNotificationService.getByKey(notificationId);

        if (userNotification.getUser().getId() == user.getId()) {
            return new ResponseEntity<>(userNotificationDtoMapper.toDto(userNotification), HttpStatus.OK);
        } else {
            log.info("UserNotification not found");
            throw new NotFoundException("UserNotification not found");
        }
    }

    @PutMapping("/{notificationId}")
    public void notificationsStatus(@PathVariable("notificationId") Long notificationId) {

//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) getSecurityUserOrNull();
        UserNotification userNotification = userNotificationService.getByKey(notificationId);

        if (userNotification.getUser().getId() == user.getId()) {
            userNotification.setShow(false);
            userNotificationService.update(userNotification);
        } else {
            log.info("UserNotification not found");
            throw new NotFoundException("UserNotification not found");
        }
    }
}
