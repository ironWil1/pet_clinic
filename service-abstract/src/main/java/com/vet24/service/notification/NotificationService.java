package com.vet24.service.notification;


import com.google.api.services.calendar.model.Event;

import com.vet24.models.notification.Notification;
import com.vet24.service.ReadWriteService;

public interface NotificationService extends ReadWriteService<Long, Notification> {

    Event createEvent(Notification notification);
}
