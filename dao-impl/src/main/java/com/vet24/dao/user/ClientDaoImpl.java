package com.vet24.dao.user;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Client;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ClientDaoImpl extends ReadWriteDaoImpl<Long, Client> implements ClientDao {

    @PersistenceContext
    private EntityManager entityManager;

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
}
