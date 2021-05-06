package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.User;

public interface UserDao extends ReadWriteDao<Long, User> {

    User getUserByLogin(String login);
}
