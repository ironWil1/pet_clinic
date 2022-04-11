package com.vet24.service.notification.dto;

import com.vet24.dao.notification.dto.NotificationDtoDao;
import com.vet24.models.dto.notification.MailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationDtoServiceImpl implements NotificationDtoService{

    @Autowired
    private final NotificationDtoDao notificationDtoDao;

    public NotificationDtoServiceImpl(NotificationDtoDao notificationDtoDao) {
        this.notificationDtoDao = notificationDtoDao;
    }


    @Override
    public List<MailNotification> getEmailsAndContentsForNotifications(LocalDate eventDate) {
        return notificationDtoDao.getEmailsAndContentsForNotifications(eventDate);
    }

}
