package com.vet24.web;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.User;
import com.vet24.service.pet.PetService;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.RoleService;
import com.vet24.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Component
public class TestDataInitializer implements ApplicationRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final ClientService clientService;
    private final PetService petService;
    private final Environment environment;

    public TestDataInitializer(RoleService roleService, UserService userService,
                               ClientService clientService, PetService petService, Environment environment) {
        this.roleService = roleService;
        this.userService = userService;
        this.clientService = clientService;
        this.petService = petService;
        this.environment = environment;
    }

    public void roleInitialize() {
        roleService.persist(new Role(RoleNameEnum.ADMIN));
        roleService.persist(new Role(RoleNameEnum.MANAGER));
        roleService.persist(new Role(RoleNameEnum.CLIENT));
    }

    public void userInitialize() {
        userService.persist(new User("Ivan", "Ivanov", "Ivan",
                "123456", roleService.getByKey(1L)));
        userService.persist(new User("Petr", "Petrov", "Petr",
                "123456", roleService.getByKey(2L)));
        clientService.persist(new Client("John", "Smith", "clientLogin",
                "123456", roleService.getByKey(3L), new HashSet<>()));
    }

    public void petInitialize() {
        Dog dog1 = new Dog("Delilah", LocalDate.now(), PetType.DOG, Gender.FEMALE, "Yorkshire Terrier",
                clientService.getByKey(3L));
        Dog dog2 = new Dog("Buddy", LocalDate.now(), PetType.DOG, Gender.MALE, "Golden Retriever",
                clientService.getByKey(3L));
        petService.persist(dog1);
        petService.persist(dog2);
    }

    public void userUpdateMethod() {
        User user = new User("Test", "Testov", "TestLogin",
                "TestPassword", roleService.getByKey(2L));
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

    @Override
    public void run(ApplicationArguments args) {
        if (Objects.requireNonNull(environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create")
                || Objects.requireNonNull(
                        environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create-drop")) {
            roleInitialize();
            userInitialize();
            petInitialize();
            //userUpdateMethod();
            //userDeleteMethod();
            //roleUpdateMethod();
            //roleDeleteMethod();
        }
    }
}