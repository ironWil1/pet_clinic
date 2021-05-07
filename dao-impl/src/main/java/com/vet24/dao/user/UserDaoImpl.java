package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<Long, User> implements UserDao {
    @Override
    public User getUserByLogin(String login) {
        return manager
                .createQuery("from User where login =:login", User.class)
                .setParameter("login", login).getSingleResult();
    }
}
