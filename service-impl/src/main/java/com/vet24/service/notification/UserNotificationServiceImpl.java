package com.vet24.service.notification;

import com.vet24.dao.ReadWriteDao;
import com.vet24.dao.notification.UserNotificationDao;
import com.vet24.models.notification.UserNotification;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationServiceImpl extends ReadWriteServiceImpl<Long, UserNotification> implements UserNotificationService{

    private final UserNotificationDao userNotificationDao;


    protected UserNotificationServiceImpl(ReadWriteDao<Long, UserNotification> readWriteDao, UserNotificationDao userNotificationDao) {
        super(readWriteDao);
        this.userNotificationDao = userNotificationDao;
    }
}
