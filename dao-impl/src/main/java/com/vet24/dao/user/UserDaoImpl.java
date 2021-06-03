package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<Long, User> implements UserDao {

    @Override
    public User getUserByEmail(String email) {
        return manager
                .createQuery("FROM User where email =:email", User.class)
                .setParameter("email", email).getSingleResult();
    }
}
