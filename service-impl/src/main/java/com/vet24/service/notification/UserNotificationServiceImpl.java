package com.vet24.service.notification;

import com.vet24.dao.ReadWriteDao;
import com.vet24.dao.notification.UserNotificationDao;
import com.vet24.models.notification.Notification;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationServiceImpl extends ReadWriteServiceImpl<Long, Notification> implements UserNotificationService{

    private final UserNotificationDao userNotificationDao;


    @Autowired
    public UserNotificationServiceImpl(ReadWriteDao<Long, Notification> readWriteDao, UserNotificationDao userNotificationDao) {
        super(readWriteDao);
        this.userNotificationDao = userNotificationDao;
    }
}
