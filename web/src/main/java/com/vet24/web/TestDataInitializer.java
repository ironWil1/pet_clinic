package com.vet24.web;

import com.vet24.models.user.Role;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.User;
import com.vet24.service.userService.RoleService;
import com.vet24.service.userService.UserService;

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

    public void roleInitialize() {
        roleService.addRole(new Role(RoleNameEnum.ADMIN));
        roleService.addRole(new Role(RoleNameEnum.MANAGER));
        roleService.addRole(new Role(RoleNameEnum.CLIENT));
    }

    public void userInitialize() {
        userService.addUser(new User("Ivan", "Ivanov", "Ivan", "123456", roleService.getRoleById(1L)));
        userService.addUser(new User("Petr", "Petrov", "Petr", "123456", roleService.getRoleById(2L)));
        userService.addUser(new User("Jm", "Jm", "Jm", "123456", roleService.getRoleById(3L)));
    }


    @Override
    public void run(ApplicationArguments args) {
        roleInitialize();
        userInitialize();
        System.out.println(userService.getUserById(1L).getAuthorities());
    }
}