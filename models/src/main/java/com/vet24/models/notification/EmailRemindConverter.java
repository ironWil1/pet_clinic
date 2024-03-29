package com.vet24.models.notification;

public class EmailRemindConverter implements RemindConverter{

    @Override
    public EmailRemind convert(UserNotification notification) {
        return new EmailRemind(notification.getNotification().getContent(), notification.getUser().getEmail());
    }
}
