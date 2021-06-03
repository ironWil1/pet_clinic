package com.vet24.dao.user;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Client;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class ClientDaoImpl extends ReadWriteDaoImpl<Long, Client> implements ClientDao {

    @Override
    public Client getClientByEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT c FROM Client c WHERE c.email =:email", Client.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException(email + " doesn't exist!");
        }
    }

    @Override
    public Client testGetCurrentClientEagerly() {
        try {
            return manager
                    .createQuery("SELECT c FROM Client c join fetch c.pets p WHERE p.client.id =:id", Client.class)
                    .setParameter("id", 3L).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
