package com.vet24.service.user;

import com.vet24.dao.user.UserDao;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ReadWriteServiceImpl<Long, User> implements UserService, UserDetailsService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByEmail(s);
    }
}
