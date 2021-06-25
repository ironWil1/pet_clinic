package com.vet24.web;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Client;
import com.vet24.models.user.Doctor;
import com.vet24.models.user.Manager;
import com.vet24.models.user.Role;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.DoctorService;
import com.vet24.service.user.ManagerService;
import com.vet24.service.user.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Component
@Profile("TestProfile")
public class TestDataInitializer implements ApplicationRunner {

    private final RoleService roleService;
    private final ClientService clientService;
    private final DoctorService doctorService;
    private final ManagerService managerService;

    @Autowired
    public TestDataInitializer(RoleService roleService,
                               ClientService clientService,
                               DoctorService doctorService,
                               ManagerService managerService) {
        this.roleService = roleService;
        this.clientService = clientService;
        this.doctorService = doctorService;
        this.managerService = managerService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        System.err.println("Initial fake setup data");

        roleService.persist(new Role(RoleNameEnum.ADMIN));
        roleService.persist(new Role(RoleNameEnum.CLIENT));
        roleService.persist(new Role(RoleNameEnum.MANAGER));
        roleService.persist(new Role(RoleNameEnum.UNVERIFIED_CLIENT));
        roleService.persist(new Role(RoleNameEnum.DOCTOR));

        Client admin = new Client("Admin", "Админыч", "admin@gmail.com", "password",
                new Role(RoleNameEnum.ADMIN), new ArrayList<>());
        clientService.persist(admin);

        Client client1 = new Client("Любимый", "Клиент", "client1@email.com", "client",
                new Role(RoleNameEnum.CLIENT), new ArrayList<>());
        clientService.persist(client1);

        Client client3 = new Client("Любимый", "Клиент", "user3@gmail.com", "client",
                new Role(RoleNameEnum.CLIENT), new ArrayList<>());
        clientService.persist(client3);

        Doctor doctor = new Doctor("Денис", "Проценко", "doctor33@gmail.com", "doctor",
                new Role(RoleNameEnum.DOCTOR));
        doctorService.persist(doctor);

        Manager manager = new Manager("Иван", "Петрович", "manager@gmail.com", "manager",
                new Role(RoleNameEnum.MANAGER));
        managerService.persist(manager);
    }
}