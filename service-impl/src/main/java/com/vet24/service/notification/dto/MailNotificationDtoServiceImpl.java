package com.vet24.service.notification.dto;

import com.vet24.dao.notification.dto.MailNotificationDtoDao;
import com.vet24.models.dto.notification.MailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MailNotificationDtoServiceImpl implements MailNotificationDtoService {

    private final MailNotificationDtoDao notificationDtoDao;

    public MailNotificationDtoServiceImpl(MailNotificationDtoDao mailNotificationDtoDao) {
        this.notificationDtoDao = mailNotificationDtoDao;
    }


    @Override
    public List<MailNotification> getEmailsAndContentsForNotifications(LocalDate eventDate) {
        return notificationDtoDao.getEmailsAndContentsForNotifications(eventDate);
    }

}
