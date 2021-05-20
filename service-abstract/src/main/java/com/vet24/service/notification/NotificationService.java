package com.vet24.service.notification;


import com.google.api.services.calendar.Calendar;

import com.vet24.models.notification.Notification;
import com.vet24.service.ReadWriteService;

import java.io.IOException;

public interface NotificationService extends ReadWriteService<Long, Notification> {

    Boolean createEvent(Notification notification, Calendar calendar) throws IOException;
}
