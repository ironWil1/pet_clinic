package com.vet24.service.user;

import com.vet24.dao.user.ClientDao;
import com.vet24.models.user.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientDao clientDao;

    @Transactional
    @Override
    public Client getClientById(Long id) {
        return clientDao.getClientById(id);
    }

    @Override
    public Client getClientByLogin(String login) {
        return clientDao.getClientByLogin(login);
    }

    @Transactional
    @Override
    public List<Client> getAllClients() {
        return clientDao.getAllClients();
    }

    @Transactional
    @Override
    public void addClient(Client client) {
        clientDao.addClient(client);
    }

    @Transactional
    @Override
    public void editClient(Client client) {
        clientDao.editClient(client);
    }

    @Transactional
    @Override
    public void deleteClient(Long id) {
        clientDao.deleteClient(id);
    }
}
