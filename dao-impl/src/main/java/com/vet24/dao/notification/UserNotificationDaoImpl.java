package com.vet24.dao.notification;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.UserNotification;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LocalDateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserNotificationDaoImpl extends ReadWriteDaoImpl<Long, UserNotification> implements UserNotificationDao {

    @Override
    public List<NotificationDto> getEmailsAndContentsForNotifications() {
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        notificationDtoList.addAll(
                manager.createNativeQuery(
                                "SELECT user_notification.id, user_entities.email, Notification.content, Notification.event_date " +
                                        "FROM user_notification INNER JOIN user_entities " +
                                        "ON user_notification.user_id = user_entities.id " +
                                        "INNER JOIN Notification " +
                                        "ON user_notification.notification_id = Notification.id " +
                                        "WHERE Notification.event_date = :event_date")
                        .setParameter("event_date", LocalDate.now().plusDays(1))
                        .unwrap(org.hibernate.query.Query.class)
                        .unwrap(SQLQuery.class)
                        .addScalar("id", LongType.INSTANCE)
                        .addScalar("email", StringType.INSTANCE)
                        .addScalar("content", StringType.INSTANCE)
                        .addScalar("event_date", LocalDateType.INSTANCE)
                        .setResultTransformer(Transformers.aliasToBean(NotificationDto.class))
                        .getResultList());
        return notificationDtoList;
    }

    @Override
    @Transactional
    public void changeFlagToTrue(List<NotificationDto> notificationDto) {
        for (NotificationDto n : notificationDto){
            manager.createNativeQuery("UPDATE user_notification SET is_show = NOT is_show WHERE user_notification.id = :id")
                    .setParameter("id", n.getId())
                    .executeUpdate();
        }
    }
}
