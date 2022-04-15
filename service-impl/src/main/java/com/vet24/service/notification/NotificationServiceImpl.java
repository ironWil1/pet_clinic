package com.vet24.service.notification;

import com.vet24.dao.ReadWriteDao;
import com.vet24.dao.notification.NotificationDao;
import com.vet24.models.notification.Notification;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends ReadWriteServiceImpl<Long, Notification> implements NotificationService {

    private final NotificationDao notificationDao;

    @Autowired
    public NotificationServiceImpl(ReadWriteDao<Long, Notification> readWriteDao, NotificationDao notificationDao) {
        super(readWriteDao);
        this.notificationDao = notificationDao;
    }
}
