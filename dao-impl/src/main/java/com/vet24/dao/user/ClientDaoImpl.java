package com.vet24.dao.user;


import com.vet24.models.user.Client;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ClientDaoImpl implements ClientDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Client getClientById(Long id) {
        Client client = entityManager.find(Client.class, id);
        return client;
    }

    @Override
    public Client getClientByLogin(String login) {
        try {
            Client client = entityManager
                    .createQuery("from Client where login =:login", Client.class)
                    .setParameter("login", login).getSingleResult();
            return client;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Client> getAllClients() {
        return entityManager.createQuery("SELECT c FROM Client c", Client.class)
                .getResultList();
    }

    @Override
    public void addClient(Client client) {
        entityManager.persist(client);
    }

    @Override
    public void editClient(Client client) {
        entityManager.merge(client);
    }

    @Override
    public void deleteClient(Long id) {
        entityManager.remove(getClientById(id));
    }
}
