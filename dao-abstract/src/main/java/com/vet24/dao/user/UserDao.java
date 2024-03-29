package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;

import java.util.Optional;

public interface UserDao extends ReadWriteDao<Long, User> {

    Optional<User> getByEmail(String email);

    User getWithAllCommentReactions(String email);

    User getUserWithPetsByEmail(String email);

    User getUserWithReactionsByEmail(String email);

    boolean isExistByIdAndRole(Long id, RoleNameEnum role);
}
