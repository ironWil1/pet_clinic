package com.vet24.service.notification;

import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.UserNotification;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface UserNotificationService extends ReadWriteService<Long, UserNotification> {

    List<NotificationDto> getEmailsAndContentsForNotifications();
    void changeFlagToTrue(List<NotificationDto> notificationDto);
}
