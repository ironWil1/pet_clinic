package com.vet24.service.user;

import com.vet24.dao.ReadOnlyDaoImpl;
import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.user.ClientDao;
import com.vet24.models.user.Client;

import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl extends ReadWriteServiceImpl<Long, Client> implements ClientService {

    private final ClientDao clientDao;

    protected ClientServiceImpl(ReadWriteDaoImpl<Long, Client> readWriteDao, ClientDao clientDao) {
        super(readWriteDao);
        this.clientDao = clientDao;
    }

    @Override
    public Client getClientByLogin(String login) {
        return clientDao.getClientByLogin(login);
    }
}
