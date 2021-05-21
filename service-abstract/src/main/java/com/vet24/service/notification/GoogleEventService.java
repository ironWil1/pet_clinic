package com.vet24.service.notification;

import com.google.api.client.auth.oauth2.Credential;

import com.vet24.models.dto.googleEvent.GoogleEventDto;

import java.io.IOException;

public interface GoogleEventService {

    void createEvent(GoogleEventDto googleEventDto, Credential credential) throws IOException;

}