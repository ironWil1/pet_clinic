package com.vet24.dao.notification;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.notification.UserNotification;
import org.springframework.stereotype.Repository;

@Repository
public class UserNotificationDaoImpl extends ReadWriteDaoImpl<Long, UserNotification> implements UserNotificationDao {
}
