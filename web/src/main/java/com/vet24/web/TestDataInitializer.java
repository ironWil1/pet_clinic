package com.vet24.web;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.models.medicine.Medicine;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.procedure.EchinococcusProcedureService;
import com.vet24.service.pet.procedure.ExternalParasiteProcedureService;
import com.vet24.service.pet.procedure.ProcedureService;
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
import java.util.HashSet;


@Component
public class TestDataInitializer implements ApplicationRunner {

    private RoleService roleService;
    private UserService userService;
    private ClientService clientService;
    private MedicineService medicineService;
    private VaccinationProcedureService vaccinationProcedureService;
    private ExternalParasiteProcedureService externalParasiteProcedureService;
    private EchinococcusProcedureService echinococcusProcedureService;
    private ReproductionService reproductionService;

    @Autowired
    Environment environment;

    @Autowired
    public TestDataInitializer(RoleService roleService, UserService userService, ClientService clientService,
                               MedicineService medicineService, VaccinationProcedureService vaccinationProcedureService,
                               ExternalParasiteProcedureService externalParasiteProcedureService,
                               EchinococcusProcedureService echinococcusProcedureService,
                               ReproductionService reproductionService) {
        this.roleService = roleService;
        this.userService = userService;
        this.clientService = clientService;
        this.medicineService = medicineService;
        this.vaccinationProcedureService = vaccinationProcedureService;
        this.externalParasiteProcedureService = externalParasiteProcedureService;
        this.echinococcusProcedureService = echinococcusProcedureService;
        this.reproductionService = reproductionService;
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

    public void procedureInitializer(){
        vaccinationProcedureService.persist(new VaccinationProcedure(
                LocalDate.now(), "nbr3br3n", false, null,
                medicineService.getByKey(1L)
        ));
        vaccinationProcedureService.getByKey(1L);

        externalParasiteProcedureService.persist(new ExternalParasiteProcedure(
                LocalDate.now(), "5g567b", true, 40,
                medicineService.getByKey(1L)
        ));
        externalParasiteProcedureService.getByKey(2L);

        echinococcusProcedureService.persist(new EchinococcusProcedure(
                LocalDate.now(), "43h5j3", true, 20,
                medicineService.getByKey(1L)
        ));
        echinococcusProcedureService.getByKey(2L);
    }

    public void reproductionInitializer(){
        reproductionService.persist(new Reproduction(
                LocalDate.now(), LocalDate.now(), LocalDate.now(), 2
        ));
        reproductionService.getByKey(1L);
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

            procedureInitializer();
            reproductionInitializer();
        }
    }
}