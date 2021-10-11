package com.vet24.service.user;

import com.vet24.models.user.Client;
import com.vet24.service.ReadWriteService;

public interface ClientService extends ReadWriteService<Long, Client> {

    Client getClientByEmail(String email);

    Client getCurrentClientWithPets();

    Client getCurrentClientWithReactions();

    Client getCurentClientEasy();

}
