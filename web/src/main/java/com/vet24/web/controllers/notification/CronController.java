package com.vet24.web.controllers.notification;

import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.service.media.MailService;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
public class CronController {

    @PersistenceContext
    private final EntityManager entityManager;

    private final MailService mailService;

    public CronController(EntityManager entityManager, MailService mailService) {
        this.entityManager = entityManager;
        this.mailService = mailService;
    }

    @GetMapping(value = {"/api/admin/cron/notification"})
    @Transactional
    public void checkNotifications() {
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        notificationDtoList.addAll(
                entityManager.createNativeQuery(
                                "SELECT user_notification.id, user_entities.email, Notification.content, Notification.event_date " +
                                        "FROM user_notification INNER JOIN user_entities " +
                                        "ON user_notification.user_id = user_entities.id " +
                                        "INNER JOIN Notification " +
                                        "ON user_notification.notification_id = Notification.id " +
                                        "WHERE Notification.event_date = :event_date")
                        .setParameter("event_date", LocalDate.now().plusDays(1))
                        .unwrap(org.hibernate.query.Query.class)
                        .setResultTransformer(Transformers.aliasToBean(NotificationDto.class))
                        .getResultList());
        for (NotificationDto n : notificationDtoList){
            mailService.sendNotificationMassage(n.getEmail(),n.getContent());
            entityManager.createNativeQuery("UPDATE user_notification SET is_show = NOT is_show WHERE user_notification.id = :id")
                    .setParameter("id", n.getId())
                    .executeUpdate();
        }
    }
}
