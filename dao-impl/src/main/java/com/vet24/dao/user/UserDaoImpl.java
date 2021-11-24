package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<Long, User> implements UserDao {

    public static final String EMAIL = "email";

    @Override
    public Optional<User> getByEmail(String email) {
        return manager
                .createQuery("FROM User where email = :email", User.class)
                .setParameter(EMAIL, email)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public User getByUserEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT u FROM User u WHERE u.email =:email", User.class)
                    .setParameter(EMAIL, email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getWithAllCommentReactions(String email) {
        try {
            return manager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.commentReactions WHERE u.email=:email", User.class)
                    .setParameter(EMAIL, email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
