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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Properties;


@Component
public class TestDataInitializer implements ApplicationRunner {

    private RoleService roleService;
    private UserService userService;
    private ClientService clientService;

    @Autowired
    Environment environment;


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
        userService.persist(new User("Ivan", "Ivanov", "Ivan", "123456", roleService.getRoleById(1L)));
        userService.persist(new User("Petr", "Petrov", "Petr", "123456", roleService.getRoleById(2L)));
        clientService.addClient(new Client("Jm", "Jm", "Jm", "123456", roleService.getRoleById(3L), new HashSet<Pet>()));

    }

    public void userUpdateMethod() {
        User user = new User("Test", "Testov", "TestLogin", "TestPassword", roleService.getRoleById(2L));
        user.setId(1L);
        userService.update(user);
    }

    public void userDeleteMethod() {
        User user = userService.getByKey(1L);
        userService.delete(user);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create")
                || environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create-drop")) {
            roleInitialize();
            userInitialize();
           // userUpdateMethod();
            userDeleteMethod();
        }
    }
}