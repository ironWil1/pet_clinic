package com.vet24.service.user;

import com.vet24.models.user.User;
import com.vet24.service.ReadWriteService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends ReadWriteService<Long, User>, UserDetailsService {

}
