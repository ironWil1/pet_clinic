package com.vet24.service.user;


import com.vet24.models.user.Client;

import java.util.List;


public interface ClientService {
    Client getClientById(Long id);
    Client getClientByLogin(String login);
    List<Client> getAllClients();
    void addClient(Client client);
    void editClient(Client client);
    void deleteClient(Long id);
}
