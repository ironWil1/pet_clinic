package com.vet24.service.notification.dto;

import com.vet24.models.dto.notification.MailNotification;

import java.time.LocalDate;
import java.util.List;

public interface NotificationDtoService {

    List<MailNotification> getEmailsAndContentsForNotifications(LocalDate eventDate);

}
