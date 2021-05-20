package com.vet24.web;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetType;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Dog;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.procedure.EchinococcusProcedureService;
import com.vet24.service.pet.procedure.ExternalParasiteProcedureService;
import com.vet24.service.pet.procedure.VaccinationProcedureService;
import com.vet24.service.pet.reproduction.ReproductionService;
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
        userService.persist(new User("Ivan", "Ivanov", "Ivan@gmail.com",
                "123456", new Role(RoleNameEnum.ADMIN)));
        userService.persist(new User("Petr", "Petrov", "Petr@gmail.com",
                "123456",  new Role(RoleNameEnum.MANAGER)));
        clientService.persist(new Client("John", "Smith", "clientLogin@gmail.com",
                "123456",  new Role(RoleNameEnum.CLIENT), new HashSet<>()));
        clientService.persist(new Client("John", "Smith", "petclinic.vet24@gmail.com",
                "123456",  new Role(RoleNameEnum.CLIENT), new HashSet<>()));
    }

    public void petInitialize() {
        Dog dog1 = new Dog("Delilah", LocalDate.now(), Gender.FEMALE, "Yorkshire Terrier",
                clientService.getByKey(3L));
        Dog dog2 = new Dog("Buddy", LocalDate.now(), Gender.MALE, "Golden Retriever",
                clientService.getByKey(4L));
        petService.persist(dog1);
        petService.persist(dog2);
    }

    public void userUpdateMethod() {
        User user = new User("Test", "Testov", "TestLogin@gmail.com",
                "TestPassword", new Role(RoleNameEnum.MANAGER));
        user.setId(1L);
        userService.update(user);
    }

    public void roleUpdateMethod() {
        Role role = new Role(RoleNameEnum.ADMIN);
        roleService.update(role);
    }

    public void userDeleteMethod() {
        User user = userService.getByKey(1L);
        userService.delete(user);
    }

    //Delete method doesn't work if user with this.Role exists in DB.
    public void roleDeleteMethod() {
        Role role = new Role(RoleNameEnum.CLIENT);
        roleService.delete(role);
    }

    public void medicineInitialize() {
        medicineService.persist(new Medicine("sinopharm", "sputnik", "sdasd",
                "protiv covid"));
        medicineService.getByKey(1L);
    }

    public void procedureInitializer(){
        vaccinationProcedureService.persist(new VaccinationProcedure(
                LocalDate.now(), "nbr3br3n", false, null,
                medicineService.getByKey(1L), petService.getByKey(1L)
        ));
        vaccinationProcedureService.getByKey(1L);

        externalParasiteProcedureService.persist(new ExternalParasiteProcedure(
                LocalDate.now(), "5g567b", true, 40,
                medicineService.getByKey(1L), petService.getByKey(1L)
        ));
        externalParasiteProcedureService.getByKey(2L);

        echinococcusProcedureService.persist(new EchinococcusProcedure(
                LocalDate.now(), "43h5j3", true, 20,
                medicineService.getByKey(1L), petService.getByKey(2L)
        ));
        echinococcusProcedureService.getByKey(3L);
    }

    public void reproductionInitializer(){
        reproductionService.persist(new Reproduction(
                LocalDate.now(), LocalDate.now(), LocalDate.now(), 2, petService.getByKey(1L)
        ));
        reproductionService.getByKey(1L);
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

            //userUpdateMethod();
            //userDeleteMethod();
            //roleUpdateMethod();
            //roleDeleteMethod();

            procedureInitializer();
            reproductionInitializer();
        }
    }
}