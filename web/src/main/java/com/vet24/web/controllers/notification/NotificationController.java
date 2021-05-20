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
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.mappers.notification.NotificationMapper;
import com.vet24.models.notification.Notification;
import com.vet24.models.user.User;
import com.vet24.service.notification.NotificationService;
import com.vet24.service.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper, UserService userService) {
        this.notificationMapper = notificationMapper;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);

    //must be email of authorize user
    private String USER = "petclinic.vet24@gmail.com";

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
    @Operation(summary = "redirect for google authorization window")
    @GetMapping(value = {"/notification"})
    public void doGoogleSignIn(HttpServletResponse response) throws IOException {

        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(CALLBACK_URI).setAccessType("offline").build();
        response.sendRedirect(redirectURL);
    }

    //redirect back to server with accesstoken
    @Operation(summary = "receive and save auth token")
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
    @Operation(summary = "create event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Dont have access for this users"),
    })
    @PostMapping(value = {"/notification/create"})
    private ResponseEntity<NotificationDto> createEvent(@RequestBody NotificationDto notificationDto) throws  Exception {
        List<User> listUser = notificationDto.getListUser();
        int count = 0;
        for (int i = 0; i < listUser.size(); i++) {
            User user = userService.getByKey(listUser.get(i).getId());
            if (user == null) { continue; }
            Credential credential = flow.loadCredential(user.getLogin());
            if (credential == null) { continue; }
            Calendar calendar = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("Petclinic").build();
            Notification notification = notificationMapper.notificationDtoToNotification(notificationDto);
            notification.setUser(user);
            notificationService.persist(notification);
            notificationService.createEvent(notification, calendar);
            count++;
        }
        if (count >= 1) {
            return new ResponseEntity<>(notificationDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(notificationDto, HttpStatus.BAD_REQUEST);
        }
    }
}
