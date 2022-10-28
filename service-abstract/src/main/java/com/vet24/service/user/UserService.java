package com.vet24.service.user;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends ReadWriteService<Long, User>, UserDetailsService {
    User getWithAllCommentReactions(String email);

    Optional<User> getUserByEmail(String email);

    User getCurrentClientWithPets();

    User getCurrentClientWithReactions();

    User getCurrentUser();

    boolean isExistByIdAndRole(Long id, RoleNameEnum role);
}
