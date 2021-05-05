package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.User;

import java.util.List;

public interface UserDao extends ReadWriteDao<Long, User> {

    User getUserByLogin(String login);
}
