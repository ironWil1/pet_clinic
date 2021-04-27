package com.vet24.web;

import com.vet24.dao.RoleDao;
import com.vet24.models.Role;
import com.vet24.models.RoleNameEnum;
import com.vet24.models.User;
import com.vet24.service.RoleService;
import com.vet24.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitializer implements ApplicationRunner {

    private RoleService roleService;
    private UserService userService;

    @Autowired
    public TestDataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Role admin = new Role(RoleNameEnum.ADMIN);
        Role client = new Role(RoleNameEnum.CLIENT);
        Role manger = new Role(RoleNameEnum.MANGER);
        roleService.addRole(admin);
        roleService.addRole(client);
        roleService.addRole(manger);
        userService.addUser(new User("Ivan", "Ivanov", "Ivan", "123456", client));
        userService.addUser(new User("Petr", "Petrov", "Petr", "123456", admin));
        userService.addUser(new User("Jm", "Jm", "Jm", "123456", manger));
    }
}