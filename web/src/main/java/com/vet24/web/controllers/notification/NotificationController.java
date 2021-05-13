package com.vet24.web.controllers.notification;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import com.vet24.models.notification.Notification;
import com.vet24.service.notification.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);

    //must be email of authorize user
    private String USER = "Petclinic";

    private String CALLBACK_URI = "http://localhost:8080/oauth";
    private String gdSecretKeys = "/credentials.json";
    private String credentialsFolder = "tokens";
    private GoogleAuthorizationCodeFlow flow;

    //inizialization
    @PostConstruct
    public void init() throws IOException {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(NotificationController.class.getResourceAsStream(gdSecretKeys)));
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(credentialsFolder))).build();
    }
    //redirect for google
    @GetMapping(value = {"/notification"})
    public void doGoogleSignIn(HttpServletResponse response) throws IOException {

        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(CALLBACK_URI).setAccessType("offline").build();
        response.sendRedirect(redirectURL);
    }
    //redirect back to server with accesstoken
    @GetMapping(value = {"/oauth"})
    public void saveAuthorizationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        if (code != null) {
            saveToken(code);
        }
        response.sendRedirect("/");
    }
    //save token
    private void saveToken(String code) throws IOException {
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(CALLBACK_URI).execute();
        flow.createAndStoreCredential(response, USER);
    }
    //create event
    @PostMapping(value = {"/create"})
    private void createEvent(@RequestBody Notification notification) throws  Exception {
        Credential credential = flow.loadCredential(USER);
        Calendar calendar = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("Petclinic").build();
        notification.setId(null);
        notificationService.persist(notification);
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
                new EventAttendee().setEmail(notification.getEmail()),
        };
        event.setAttendees(Arrays.asList(attendees));


        String calendarId = "primary";
        event = calendar.events().insert(calendarId, event).setSendNotifications(true).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }



}
