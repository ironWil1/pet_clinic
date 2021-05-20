package com.vet24.service.notification;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.notification.NotificationDao;
import com.vet24.models.notification.Notification;
import com.vet24.service.ReadWriteServiceImpl;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class NotificationServiceImpl extends ReadWriteServiceImpl<Long, Notification> implements NotificationService {

    private final NotificationDao notificationDao;

    protected NotificationServiceImpl(ReadWriteDaoImpl<Long, Notification> readWriteDao, NotificationDao notificationDao) {
        super(readWriteDao);
        this.notificationDao = notificationDao;
    }

    @Override
    public Boolean createEvent(Notification notification, Calendar calendar) {
        Event event = new Event()
                .setSummary(String.valueOf(notification.getSummary()))
                .setLocation("Moscow, Moskvorechie str 2")
                .setDescription(notification.getDescription());

        DateTime startDateTime = new DateTime(notification.getStartDate());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Moscow");
        event.setStart(start);

        DateTime endDateTime = new DateTime(notification.getEndDate());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Moscow");
        event.setEnd(end);
        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(30),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail(notification.getUser().getEmail()),
        };
        event.setAttendees(Arrays.asList(attendees));
        try {
            calendar.events().insert("primary", event).setSendNotifications(true).execute();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
