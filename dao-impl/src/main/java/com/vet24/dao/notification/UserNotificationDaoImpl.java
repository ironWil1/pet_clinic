package com.vet24.dao.notification;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.notification.UserNotification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserNotificationDaoImpl extends ReadWriteDaoImpl<Long, UserNotification> implements UserNotificationDao {

    @Override
    @Transactional
    public void changeFlagToTrue(List<Long> idList) {
            manager.createQuery("UPDATE UserNotification SET isShow = true WHERE id IN (:id)")
                    .setParameter("id", idList)
                    .executeUpdate();
    }

    @Override
    public List<UserNotification> getAllUserNotificationFromUser(Long userId) {
            return manager
                    .createQuery("FROM UserNotification un " +
                            "WHERE un.user.id = :userId", UserNotification.class)
                    .setParameter("userId", userId)
                    .getResultList();
    }

}
