package com.vet24.service.user;

import com.vet24.dao.ReadOnlyDaoImpl;
import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.user.UserDao;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ReadWriteServiceImpl<Long, User> implements UserService {

    private final UserDao userDao;

    protected UserServiceImpl(ReadWriteDaoImpl<Long, User> readWriteDao, UserDao userDao) {
        super(readWriteDao);
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByLogin(s);
    }
}
