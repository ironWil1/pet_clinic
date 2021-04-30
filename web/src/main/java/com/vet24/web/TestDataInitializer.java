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

    private final Set<Pet> pets = new HashSet<>();
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
        roleService.addRole(new Role(RoleNameEnum.ADMIN));
        roleService.addRole(new Role(RoleNameEnum.MANAGER));
        roleService.addRole(new Role(RoleNameEnum.CLIENT));
    }

    public void userInitialize() {
        userService.addUser(new User("Ivan", "Ivanov", "Ivan",
                "123456", roleService.getRoleById(1L)));
        userService.addUser(new User("Petr", "Petrov", "Petr",
                "123456", roleService.getRoleById(2L)));
        clientService.addClient(new Client("John", "Smith", "clientLogin",
                "123456", roleService.getRoleById(3L), pets));

    }

    public void petInitialize() {
        Dog dog1 = new Dog("Delilah", LocalDate.now(), PetType.DOG, Gender.FEMALE, "Yorkshire Terrier");
        Dog dog2 = new Dog("Buddy", LocalDate.now(), PetType.DOG, Gender.MALE, "Golden Retriever");
        petService.save(dog1);
        petService.save(dog2);
        pets.add(dog1);
        pets.add(dog2);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (Objects.requireNonNull(environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create")
                || Objects.requireNonNull(
                        environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create-drop")) {
            petInitialize();
            roleInitialize();
            userInitialize();
        }
    }
}