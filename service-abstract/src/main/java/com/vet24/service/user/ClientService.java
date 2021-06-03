package com.vet24.service.user;

import com.vet24.models.user.Client;
import com.vet24.service.ReadWriteService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ClientService extends ReadWriteService<Long, Client> {

    Client getClientByEmail(String email);

    Client getCurrentClient(); // temporary solution. Always returns Client with id = 3

    Client testGetCurrentClientEagerly();
}
