package com.vet24.web;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.models.medicine.Medicine;
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
        roleService.persist(new Role(RoleNameEnum.ADMIN));
        roleService.persist(new Role(RoleNameEnum.MANAGER));
        roleService.persist(new Role(RoleNameEnum.CLIENT));
    }

    public void userInitialize() {
        userService.persist(new User("Ivan", "Ivanov", "Ivan", "123456", roleService.getByKey(1L)));
        userService.persist(new User("Petr", "Petrov", "Petr", "123456", roleService.getByKey(2L)));
        clientService.persist(new Client("Jm", "Jm", "Jm", "123456", roleService.getByKey(3L), new HashSet<Pet>()));

    }

    public void userUpdateMethod() {
        User user = new User("Test", "Testov", "TestLogin", "TestPassword", roleService.getByKey(2L));
        user.setId(1L);
        userService.update(user);
    }

    public void roleUpdateMethod() {
        Role role = new Role(RoleNameEnum.ADMIN);
        role.setId(3L);
        roleService.update(role);
    }

    public void userDeleteMethod() {
        User user = userService.getByKey(1L);
        userService.delete(user);
    }

    //Delete method doesn't work if user with this.Role exists in DB.
    public void roleDeleteMethod() {
        Role role = roleService.getByKey(3L);
        roleService.delete(role);
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

            //userUpdateMethod();
            //userDeleteMethod();
            //roleUpdateMethod();
            //roleDeleteMethod();
        }
    }
}