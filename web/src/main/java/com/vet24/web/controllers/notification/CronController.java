package com.vet24.web.controllers.notification;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.service.media.MailService;
import com.vet24.service.notification.UserNotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CronController {

    private final UserNotificationService userNotificationService;
    private final MailService mailService;

    public CronController(UserNotificationService userNotificationService, MailService mailService) {
        this.userNotificationService = userNotificationService;
        this.mailService = mailService;
    }

    @GetMapping(value = {"/api/admin/cron/notification"})
    public void checkNotifications() {
        List<NotificationDto> notificationDtoList = userNotificationService.getEmailsAndContentsForNotifications();
        userNotificationService.changeFlagToTrue(notificationDtoList);
        mailService.sendNotificationMassage(notificationDtoList);
    }
}
