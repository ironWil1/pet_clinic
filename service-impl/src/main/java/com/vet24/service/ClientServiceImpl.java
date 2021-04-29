package com.vet24.service;

import com.vet24.dao.userDao.ClientDao;
import com.vet24.models.user.Client;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Transactional
    @Override
    public Client getClientById(Long id) {
        return clientDao.getClientById(id);
    }
}
