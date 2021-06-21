package com.vet24.service.user;

import com.vet24.models.user.User;
import com.vet24.service.ReadWriteService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends ReadWriteService<Long, User>, UserDetailsService {

}
