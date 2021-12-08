package com.vet24.web.controllers.notification;

import com.vet24.models.dto.google_event.GoogleEventDto;
import com.vet24.models.exception.CredentialException;
import com.vet24.models.exception.EventException;
import com.vet24.models.user.Client;
import com.vet24.service.notification.GoogleEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
public class NotificationController {

    private final GoogleEventService googleEventService;

    public NotificationController(GoogleEventService googleEventService) {
        this.googleEventService = googleEventService;
    }


    @Operation(summary = "redirect for google authorization window")
    @GetMapping(value = {"/api/notification"})
    public void doGoogleSignIn(HttpServletResponse response) throws IOException {
        String redirectURL = googleEventService.getRedirectUrl();
        log.info("The redirect address is {}",redirectURL);
        response.sendRedirect(redirectURL);
    }

    //redirect back to server with accesstoken
    @Operation(summary = "receive and save auth token")
    @GetMapping(value = {"/api/oauth"})
    public void saveAuthorizationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        if (code != null) {
            Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            googleEventService.saveToken(code, client.getEmail());
            log.info("The current client  is {}",client.getEmail());
        }
        response.sendRedirect("/");
    }

    @Operation(summary = "create event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully sent"),
            @ApiResponse(responseCode = "400", description = "Dont have credential for this user"),
            @ApiResponse(responseCode = "502", description = "Cannot create event"),
    })
    @PostMapping(value = {"/api/notification/create"})
    public ResponseEntity<GoogleEventDto> createEvent(@RequestBody GoogleEventDto googleEventDto) throws IOException {
        try {
            googleEventService.createEvent(googleEventDto);
            log.info("The google event  is {}",googleEventDto.getDescription());
            return new ResponseEntity<>(googleEventDto, HttpStatus.OK);
        } catch (CredentialException e) {
            return new ResponseEntity<>(googleEventDto, HttpStatus.BAD_REQUEST);
        } catch (EventException e) {
            return new ResponseEntity<>(googleEventDto, HttpStatus.BAD_GATEWAY);
        }
    }

    @Operation(summary = "edit event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited"),
            @ApiResponse(responseCode = "502", description = "Cannot edit event"),
    })
    @PostMapping(value = {"/api/notification/edit"})
    public ResponseEntity<GoogleEventDto> editEvent(@RequestBody GoogleEventDto googleEventDto) throws IOException {
        try {
            googleEventService.editEvent(googleEventDto);
            log.info("The edited google event  is {}",googleEventDto.getDescription());
            return new ResponseEntity<>(googleEventDto, HttpStatus.OK);
        } catch (EventException e) {
            return new ResponseEntity<>(googleEventDto, HttpStatus.BAD_GATEWAY);
        }
    }

    @Operation(summary = "delete event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "502", description = "Cannot delete event"),
    })
    @PostMapping(value = {"/api/notification/delete"})
    public ResponseEntity<GoogleEventDto> deleteEvent(@RequestBody GoogleEventDto googleEventDto) throws IOException {
        try {
            googleEventService.deleteEvent(googleEventDto);
            log.info("The deleted google event  is {}",googleEventDto.getDescription());
            return new ResponseEntity<>(googleEventDto, HttpStatus.OK);
        } catch (EventException e) {
            return new ResponseEntity<>(googleEventDto, HttpStatus.BAD_GATEWAY);
        }
    }
}