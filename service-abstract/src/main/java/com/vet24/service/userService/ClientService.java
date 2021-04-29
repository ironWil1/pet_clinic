package com.vet24.service.userService;


import com.vet24.models.user.Client;

import java.util.List;


public interface ClientService {
    Client getClientById(Long id);
    List<Client> getAllClients();
    void addClient(Client client);
    void editClient(Client client);
    void deleteClient(Long id);
}
