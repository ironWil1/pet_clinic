package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
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

    public User getWithAllCommentReactions(String email) {
        try {
            return manager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.commentReactions WHERE u.email=:email", User.class)
                    .setParameter(EMAIL, email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getUserWithPetsByEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT u FROM User u JOIN FETCH u.pets WHERE u.email =:email", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getUserWithReactionsByEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT c FROM User c JOIN FETCH c.commentReactions WHERE c.email =:email", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean isExistByIdAndRole(Long id, RoleNameEnum role) {
        return manager
                .createQuery("SELECT CASE WHEN (count(id)>0) then true else false end" +
                        " FROM User WHERE id = :id AND role.name = :role", Boolean.class)
                .setParameter("id", id)
                .setParameter("role", role)
                .getSingleResult();
    }
}
