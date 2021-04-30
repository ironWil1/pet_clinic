package com.vet24.service.user;

import com.vet24.models.user.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    User getUserById(Long id);
    List<User> getAllUsers();
    void addUser(User user);
    void editUser(User user);
    void deleteUser(Long id);
}
