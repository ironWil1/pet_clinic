package com.vet24.web;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.EchinococcusProcedureService;
import com.vet24.service.pet.procedure.ExternalParasiteProcedureService;
import com.vet24.service.pet.procedure.VaccinationProcedureService;
import com.vet24.service.pet.reproduction.ReproductionService;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.RoleService;
import com.vet24.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;


@Component
public class TestDataInitializer implements ApplicationRunner {

    private final VaccinationProcedureService vaccinationProcedureService;
    private final ExternalParasiteProcedureService externalParasiteProcedureService;
    private final EchinococcusProcedureService echinococcusProcedureService;
    private final ReproductionService reproductionService;
    private final RoleService roleService;
    private final UserService userService;
    private final ClientService clientService;
    private final PetService petService;
    private final MedicineService medicineService;

    private final Environment environment;

    private final Role ADMIN = new Role(RoleNameEnum.ADMIN);
    private final Role MANAGER = new Role(RoleNameEnum.MANAGER);
    private final Role CLIENT = new Role(RoleNameEnum.CLIENT);
    private final Set<Pet> PETS = new HashSet<>();
    private final Gender MALE = Gender.MALE;
    private final Gender FEMALE = Gender.FEMALE;

    @Autowired
    public TestDataInitializer(PetService petService, RoleService roleService, UserService userService, ClientService clientService,
                               MedicineService medicineService, VaccinationProcedureService vaccinationProcedureService,
                               ExternalParasiteProcedureService externalParasiteProcedureService,
                               EchinococcusProcedureService echinococcusProcedureService,
                               ReproductionService reproductionService, Environment environment) {
        this.roleService = roleService;
        this.userService = userService;
        this.clientService = clientService;
        this.petService = petService;
        this.medicineService = medicineService;
        this.vaccinationProcedureService = vaccinationProcedureService;
        this.externalParasiteProcedureService = externalParasiteProcedureService;
        this.echinococcusProcedureService = echinococcusProcedureService;
        this.reproductionService = reproductionService;
        this.environment = environment;
    }

    public void roleInitialize() {
        roleService.persist(new Role(RoleNameEnum.ADMIN));
        roleService.persist(new Role(RoleNameEnum.MANAGER));
        roleService.persist(new Role(RoleNameEnum.CLIENT));
    }

    public void userInitialize() {
        List<User> users = new ArrayList<>();
        List<Client> clients = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            clients.add(new Client("ClientFirstName" + i, "ClientLastName" + i, "ClientEmail" + i, "ClientPassword" + i, CLIENT, PETS));
            if(i % 2 == 0) {
                users.add(new User("AdminFirstName" + i, "AdminLastName" + i, "AdminEmail" + i, "AdminPassword" + i, ADMIN));
            } else {
                users.add(new User("ManagerFirstName" + i, "ManagerLastName" + i, "ManagerEmail" + i, "ManagerPassword" + i, MANAGER));
            }
        }
        userService.persistAll(users);
        clientService.persistAll(clients);
    }

    public void petInitialize() {
        List<Pet> pets = new ArrayList<>();
        long id = 31L;
        for (int i = 1; i <= 30; i++) {
            if (i % 2 == 0) {
                pets.add(new Dog("DogName" + i, LocalDate.now(), MALE, "DogBreed" + i, clientService.getByKey(id++)));
            } else {
                pets.add(new Cat("CatName" + i, LocalDate.now(), FEMALE, "CatBreed" + i, clientService.getByKey(id++)));
            }
        }

        petService.persistAll(pets);
    }

    public void medicineInitialize() {
        List<Medicine> medicines = new ArrayList<>();
        for(int i = 1; i <= 30; i++) {
            medicines.add(new Medicine("manufactureName" + i, "name" + i, "icon" + i, "description" + i));
        }
        medicineService.persistAll(medicines);
    }

    public void procedureInitializer(){
        List<VaccinationProcedure> vaccination = new ArrayList<>();
        List<ExternalParasiteProcedure> externalParasite = new ArrayList<>();
        List<EchinococcusProcedure> echinococcus = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            if (i <= 10) {
                vaccination.add(new VaccinationProcedure(LocalDate.now(), "VaccinationMedicineBatchNumber" + i,
                        false, i, medicineService.getByKey((long) i), petService.getByKey((long) i)));
            }
            if (i > 10 && i <= 20) {
                externalParasite.add(new ExternalParasiteProcedure(LocalDate.now(), "ExternalParasiteMedicineBatchNumber" + i,
                        true, i, medicineService.getByKey((long)i), petService.getByKey((long) i)));
            }
            if (i > 20) {
                echinococcus.add(new EchinococcusProcedure(LocalDate.now(), "EchinococcusMedicineBatchNumber" + i,
                        true, i, medicineService.getByKey((long) i),  petService.getByKey((long) i)));
            }
        }

        vaccinationProcedureService.persistAll(vaccination);
        externalParasiteProcedureService.persistAll(externalParasite);
        echinococcusProcedureService.persistAll(echinococcus);
    }

    public void reproductionInitializer(){
        List<Reproduction> reproductions = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            reproductions.add(new Reproduction(LocalDate.now(), LocalDate.now(), LocalDate.now(), i, petService.getByKey((long) i)));
        }
        reproductionService.persistAll(reproductions);
    }


    @Override
    public void run(ApplicationArguments args) {
        if (Objects.requireNonNull(environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create")
                || Objects.requireNonNull(
                        environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create-drop")) {
            roleInitialize();
            userInitialize();
            petInitialize();
            medicineInitialize();
            procedureInitializer();
            reproductionInitializer();
        }
    }
}