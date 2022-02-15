package com.vet24.service.notification.dto;

import com.vet24.models.dto.notification.NotificationDto;

import java.time.LocalDate;
import java.util.List;

public interface NotificationDtoService {

    List<NotificationDto> getEmailsAndContentsForNotifications(LocalDate eventDate);

    List<Long> getIdFromNotificationDtoList (List<NotificationDto> notificationDtoList);
}
