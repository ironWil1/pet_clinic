package com.vet24.dao.notification.dto;

import com.vet24.models.dto.notification.MailNotification;

import java.time.LocalDate;
import java.util.List;

public interface NotificationDtoDao {

    List<MailNotification> getEmailsAndContentsForNotifications(LocalDate eventDate);

}
