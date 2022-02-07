package com.vet24.web.controllers.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "redirect for google authorization window")
    @GetMapping(value = {"/api/notification"})
    public void doGoogleSignIn(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }

    //redirect back to server with accesstoken
    @Operation(summary = "receive and save auth token")
    @GetMapping(value = {"/api/oauth"})
    public void saveAuthorizationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }

    @Operation(summary = "create event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully sent"),
            @ApiResponse(responseCode = "400", description = "Dont have credential for this user"),
            @ApiResponse(responseCode = "502", description = "Cannot create event"),
    })
    @PostMapping(value = {"/api/notification/create"})
    public ResponseEntity<Object> createEvent(@RequestBody Object object) throws IOException {
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @Operation(summary = "edit event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited"),
            @ApiResponse(responseCode = "502", description = "Cannot edit event"),
    })
    @PostMapping(value = {"/api/notification/edit"})
    public ResponseEntity<Object> editEvent(@RequestBody Object object) throws IOException {
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @Operation(summary = "delete event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "502", description = "Cannot delete event"),
    })
    @PostMapping(value = {"/api/notification/delete"})
    public ResponseEntity<Object> deleteEvent(@RequestBody Object object) throws IOException {
        return new ResponseEntity<>(object, HttpStatus.OK);
    }
}

