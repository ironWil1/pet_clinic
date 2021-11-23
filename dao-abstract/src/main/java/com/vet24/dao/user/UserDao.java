package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.User;

import java.util.Optional;

public interface UserDao extends ReadWriteDao<Long, User> {

    User findUserByUsername(String username);

    Optional<User> getByEmail(String email);

    User getByUserEmail(String email);

    User getWithAllCommentReactions(String email);
}
