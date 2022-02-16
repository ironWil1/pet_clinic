package com.vet24.web.controllers.notification;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.service.media.MailService;
import com.vet24.service.notification.UserNotificationService;
import com.vet24.service.notification.dto.NotificationDtoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class CronController {

    private final UserNotificationService userNotificationService;
    private final MailService mailService;
    private final NotificationDtoService notificationDtoService;

    public CronController(UserNotificationService userNotificationService, MailService mailService, NotificationDtoService notificationDtoService) {
        this.userNotificationService = userNotificationService;
        this.mailService = mailService;
        this.notificationDtoService = notificationDtoService;
    }

    @GetMapping(value = {"/api/admin/cron/notification"})
    public void checkNotifications() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<NotificationDto> notificationDtoList = notificationDtoService.getEmailsAndContentsForNotifications(tomorrow);
        userNotificationService.changeFlagToTrue(notificationDtoList.stream().map(NotificationDto::getId).collect(Collectors.toList()));
        mailService.sendNotificationMassage(notificationDtoList);
    }
}
