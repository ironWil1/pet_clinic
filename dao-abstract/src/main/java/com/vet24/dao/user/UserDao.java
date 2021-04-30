package com.vet24.dao.user;

import com.vet24.models.user.User;

import java.util.List;

public interface UserDao {
    User getUserById(Long id);
    User getUserByLogin(String login);
    List<User> getAllUsers();
    void addUser(User user);
    void editUser(User user);
    void deleteUser(Long id);
}
