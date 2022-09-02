package com.vet24.web.controllers.notification;

import com.vet24.models.dto.notification.UserNotificationDto;
import com.vet24.models.mappers.dto.UserNotificationDtoMapper;
import com.vet24.models.notification.UserNotification;
import com.vet24.models.user.User;
import com.vet24.service.notification.UserNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.util.List;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;


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
        return getOptionalOfNullableSecurityUser()
                .map(User::getId)
                .map(userNotificationService::getAllUserNotificationByUserId)
                .map(userNotificationDtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Receive notification by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification received"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @GetMapping("/{notificationId}")
    public ResponseEntity<UserNotificationDto> getUserNotificationById(@PathVariable("notificationId") Long notificationId) {
        UserNotification userNotification = userNotificationService.getByKey(notificationId);
        return getOptionalOfNullableSecurityUser().map(User::getId)
                .filter(userNotification.getUser().getId()::equals)
                .map(x -> userNotificationDtoMapper.toDto(userNotification))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.info("UserNotification not found");
                    throw new NotFoundException("UserNotification not found");
                });
    }

    @PutMapping("/{notificationId}")
    public void notificationsStatus(@PathVariable("notificationId") Long notificationId) {
        UserNotification userNotification = userNotificationService.getByKey(notificationId);
        getOptionalOfNullableSecurityUser().map(User::getId)
                .filter(userNotification.getUser().getId()::equals)
                .map(x -> {
                    userNotification.setShow(false);
                    return userNotificationService.update(userNotification);
                }).orElseThrow(() -> {
                    log.info("UserNotification not found");
                    throw new NotFoundException("UserNotification not found");
                });
    }
}
