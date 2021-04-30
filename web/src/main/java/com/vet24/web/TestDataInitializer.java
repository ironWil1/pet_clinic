package com.vet24.web;

import com.vet24.models.pet.Pet;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.User;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.RoleService;
import com.vet24.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;


@Component
public class TestDataInitializer implements ApplicationRunner {

    private RoleService roleService;
    private UserService userService;
    private ClientService clientService;


    @Autowired
    public TestDataInitializer(RoleService roleService, UserService userService, ClientService clientService) {
        this.roleService = roleService;
        this.userService = userService;
        this.clientService = clientService;
    }

    public void roleInitialize() {
        roleService.addRole(new Role(RoleNameEnum.ADMIN));
        roleService.addRole(new Role(RoleNameEnum.MANAGER));
        roleService.addRole(new Role(RoleNameEnum.CLIENT));
    }

    public void userInitialize() {
        userService.addUser(new User("Ivan", "Ivanov", "Ivan", "123456", roleService.getRoleById(1L)));
        userService.addUser(new User("Petr", "Petrov", "Petr", "123456", roleService.getRoleById(2L)));
        clientService.addClient(new Client("Jm", "Jm", "Jm", "123456", roleService.getRoleById(3L), new HashSet<Pet>()));

    }

    @Override
    public void run(ApplicationArguments args) {
        roleInitialize();
        userInitialize();

    }
}