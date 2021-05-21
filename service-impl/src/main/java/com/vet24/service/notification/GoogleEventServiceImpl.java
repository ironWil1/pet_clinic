package com.vet24.service.notification;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import com.vet24.models.dto.googleEvent.GoogleEventDto;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class GoogleEventServiceImpl implements GoogleEventService {

    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Calendar buildCalendar(Credential credential) {
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Petclinic").build();
    }

    @Override
    public void createEvent(GoogleEventDto googleEventDto, Credential credential) throws IOException {
        Event event = new Event()
                .setSummary(String.valueOf(googleEventDto.getSummary()))
                .setLocation(googleEventDto.getLocation())
                .setDescription(googleEventDto.getDescription());

        DateTime startDateTime = new DateTime(googleEventDto.getStartDate());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Moscow");
        event.setStart(start);

        DateTime endDateTime = new DateTime(googleEventDto.getEndDate());
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
                new EventAttendee().setEmail(googleEventDto.getEmail()),
        };
        event.setAttendees(Arrays.asList(attendees));
        try {
            buildCalendar(credential).events().insert("primary", event)
                    .setSendNotifications(true).execute();
        } catch (IOException e) {
            throw new IOException("cannot create event");
        }
    }
}
