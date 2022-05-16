package com.vet24.web.controllers.notification;

import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.mappers.notification.NotificationMapper;
import com.vet24.models.notification.Notification;
import com.vet24.models.notification.UserNotification;
import com.vet24.models.user.User;
import com.vet24.service.notification.NotificationService;
import com.vet24.service.notification.UserNotificationService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/admin/notification")
@Tag(name = "admin notification controller", description = "CRUD operations with notifications")
public class AdminNotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final UserNotificationService userNotificationService;
    private final UserService userService;

    public AdminNotificationController(NotificationService notificationService, NotificationMapper notificationMapper, UserNotificationService userNotificationService, UserService userService) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        this.userNotificationService = userNotificationService;
        this.userService = userService;
    }


    @Operation(summary = "Get ALL notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success get the notifications"),
            @ApiResponse(responseCode = "404", description = "notifications not found")
    })
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotificationAll() {
        List<NotificationDto> notificationList = notificationMapper.toDto(notificationService.getAll());
        return new ResponseEntity<>(notificationList, HttpStatus.OK);
    }


    @Operation(summary = "Get notification by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success get the notification by this ID"),
            @ApiResponse(responseCode = "404", description = "notification with this ID not found")
    })
    @GetMapping(value = {"/{id}"})
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable("id") Long notificationId) {
        if (notificationService.isExistByKey(notificationId)) {
            Notification notification = notificationService.getByKey(notificationId);
            return new ResponseEntity<>(notificationMapper.toDto(notification), HttpStatus.OK);
        } else {
            log.info("Notification with id {} not found", notificationId);
            throw new NotFoundException("Notification not found");
        }
    }

    @Operation(summary = "Update the notification by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success update the notification by ID"),
            @ApiResponse(responseCode = "404", description = "notification not changed")
    })
    @PutMapping(value = {"/{id}"})
    public ResponseEntity<NotificationDto> updateNotificationById(
            @PathVariable("id") Long notificationId,
            @RequestBody NotificationDto notificationDto
    ) {
        if (notificationService.isExistByKey(notificationId)) {
            Notification notification = notificationService.getByKey(notificationId);
            notificationMapper.updateEntity(notificationDto, notification);
            notificationService.update(notification);
            return new ResponseEntity<>(notificationMapper.toDto(notification), HttpStatus.OK);
        } else {
            log.info("Notification with id {} not found", notificationId);
            throw new NotFoundException("Notification not found");
        }
    }


    @Operation(summary = "Create new notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification is create",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDto.class))),
            @ApiResponse(responseCode = "404", description = "Notification not found"),
    })
    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationDto notificationDto) {
        Notification notification = notificationMapper.toEntity(notificationDto);
        notificationService.persist(notification);
        return ResponseEntity.status(200).body(notificationMapper.toDto(notification));
    }


    @Operation(summary = "Delete the notification by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification deleted"),
            @ApiResponse(responseCode = "404", description = "Notification not founded")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("id") Long notificationId) {
        if (notificationService.isExistByKey(notificationId)) {
            Notification notification = notificationService.getByKey(notificationId);
            notificationService.delete(notification);
            return ResponseEntity.ok().build();
        } else {
            log.info("Notification with id {} not found", notificationId);
            throw new NotFoundException("Notification not found");
        }
    }

    @PostMapping("/api/admin/notification/{id}/addToUser")
    public ResponseEntity<Map<Long, String>> addToUser(@RequestBody List<Long> listId, @PathVariable Long id) {
        Notification notification = notificationService.getByKey(id);
        Map<Long, String> response = new HashMap<>();
        if (notification == null) {
            throw new NotFoundException("Notification not found");
        }
        for (Long userId : listId) {
            User user = userService.getByKey(userId);
            if (user != null) {
                UserNotification userNotification = new UserNotification(notification, user, true);
                userNotificationService.persist(userNotification);
            } else {
                response.put(userId, "User with id = " + userId + " not found");
            }
        }
        return ResponseEntity.ok().body(response);
    }
}

