package com.vet24.service.notification;

import com.vet24.models.notification.EmailRemind;
import com.vet24.service.media.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailReminder implements Reminder<EmailRemind> {
    @Autowired
    private MailService mailService;

    @Value("${spring.reminder.subject}")
    private String subject;

    @Override
    public void send(EmailRemind remind) {
        mailService.sendMultipartHtmlMessage(remind.getReceiver(), subject, remind.getContent());
    }

    @Override
    public void send(List<EmailRemind> reminds) {
        for (EmailRemind remind : reminds) {
            mailService.sendMultipartHtmlMessage(remind.getReceiver(), subject, remind.getContent());
        }
    }
}
