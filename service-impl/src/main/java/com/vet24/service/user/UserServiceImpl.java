package com.vet24.service.user;

import com.vet24.dao.user.UserDao;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ReadWriteServiceImpl<Long, User> implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByEmail(s);
    }

    @Override
    public User getCurrentUser() {
        System.out.println(userDao.getByKey(3L));
        return userDao.getByKey(3L);
    }
}
