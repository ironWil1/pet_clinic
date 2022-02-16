package com.vet24.dao.notification.dto;

import com.vet24.models.dto.notification.NotificationDto;

import java.time.LocalDate;
import java.util.List;

public interface NotificationDtoDao {

    List<NotificationDto> getEmailsAndContentsForNotifications(LocalDate eventDate);

}
