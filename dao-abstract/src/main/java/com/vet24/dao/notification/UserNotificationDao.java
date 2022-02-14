package com.vet24.dao.notification;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.UserNotification;

import java.util.List;


public interface UserNotificationDao extends ReadWriteDao<Long, UserNotification> {

    List<NotificationDto> getEmailsAndContentsForNotifications();
    void changeFlagToTrue(List<NotificationDto> notificationDto);
}
