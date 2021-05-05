package com.vet24.web;

import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.User;
import com.vet24.service.medicine.MedicineService;
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
    private MedicineService medicineService;

    @Autowired
    Environment environment;


    @Autowired
    public TestDataInitializer(RoleService roleService, UserService userService, ClientService clientService, MedicineService medicineService) {
        this.roleService = roleService;
        this.userService = userService;
        this.clientService = clientService;
        this.medicineService = medicineService;
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

    public void medicineInitialize() {
        medicineService.persist(new Medicine("sinopharm", "sputnik", "sdasd", "protiv covid"));
        medicineService.getByKey(1L);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create")
                || environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create-drop")) {
            roleInitialize();
            userInitialize();
            medicineInitialize();
        }
    }
}