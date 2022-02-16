package com.vet24.dao.notification;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.notification.UserNotification;

import java.util.List;


public interface UserNotificationDao extends ReadWriteDao<Long, UserNotification> {

    void changeFlagToTrue(List<Long> idList);
}
