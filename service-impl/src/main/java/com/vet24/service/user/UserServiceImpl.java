package com.vet24.service.user;

import com.vet24.dao.user.UserDao;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends ReadWriteServiceImpl<Long, User> implements UserService {

    private final UserDao userDao;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userDao.getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s doesn't exist!", email)));
    }


    @Override
    public User getWithAllCommentReactions(String email) {
        return userDao.getWithAllCommentReactions(email);
    }

    @Override
    @Transactional()
    public void persist(User user) {
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userDao.persist(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        String newPassword = user.getPassword();
        if (passwordEncoder.upgradeEncoding(newPassword)) {
            String password = passwordEncoder.encode(newPassword);
            user.setPassword(password);
        }
        return userDao.update(user);
    }

    @Override
    @Transactional
    public void persistAll(List<User> users) {
        for (User user : users) {
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
        }
        userDao.persistAll(users);
    }

    @Override
    @Transactional
    public List<User> updateAll(List<User> users) {
        for (User user : users) {
            String newPassword = user.getPassword();
            if (passwordEncoder.upgradeEncoding(newPassword)) {
                String password = passwordEncoder.encode(newPassword);
                user.setPassword(password);
            }
        }
        return userDao.updateAll(users);
    }
}
