package com.vet24.service.user;

import com.vet24.dao.user.UserDao;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;

@Service
public class UserServiceImpl extends ReadWriteServiceImpl<Long, User> implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        super(userDao);
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
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
    public User getCurrentUser(){
        return getOptionalOfNullableSecurityUser()
                .map(User::getEmail)
                .flatMap(userDao::getByEmail)
                .orElseThrow();
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public User getCurrentClientWithPets() {
        return getOptionalOfNullableSecurityUser()
                .map(User::getUsername)
                .map(userDao::getUserWithPetsByEmail)
                .orElseThrow();
    }

    @Override
    public User getCurrentClientWithReactions() {
        return getOptionalOfNullableSecurityUser()
                .map(User::getUsername)
                .map(userDao::getWithAllCommentReactions)
                .orElseThrow();
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

    @Override
    public boolean isExistByIdAndRole(Long id, RoleNameEnum role) {
        return userDao.isExistByIdAndRole(id, role);
    }
}
