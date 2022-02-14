package com.vet24.service.notification;

import com.vet24.dao.ReadWriteDao;
import com.vet24.dao.notification.UserNotificationDao;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.UserNotification;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotificationServiceImpl extends ReadWriteServiceImpl<Long, UserNotification> implements UserNotificationService{

    private final UserNotificationDao userNotificationDao;


    protected UserNotificationServiceImpl(ReadWriteDao<Long, UserNotification> readWriteDao, UserNotificationDao userNotificationDao) {
        super(readWriteDao);
        this.userNotificationDao = userNotificationDao;
    }

    @Override
    public List<NotificationDto> getEmailsAndContentsForNotifications() {
        return userNotificationDao.getEmailsAndContentsForNotifications();
    }

    @Override
    public void changeFlagToTrue(List<NotificationDto> notificationDto) {
        userNotificationDao.changeFlagToTrue(notificationDto);
    }
}
