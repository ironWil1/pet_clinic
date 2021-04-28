package com.vet24.dao.userDaoImpl;

import com.vet24.dao.userDao.UserDao;
import com.vet24.models.user.User;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public User getUserById(Long id) {
        User user = (User) entityManager.createQuery("from User where id =:id").setParameter("id", id).getSingleResult();
        return user;
    }

    @Override
    public User getUserByLogin(String login) {
        User user = (User) entityManager.createQuery("from User where login =:login").setParameter("login", login).getSingleResult();
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> query =
                entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        entityManager.remove(getUserById(id));
    }
}
