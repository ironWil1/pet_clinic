package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<Long, User> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUserByLogin(String login) {
        return entityManager
                .createQuery("from User where login =:login", User.class)
                .setParameter("login", login).getSingleResult();
    }
}
