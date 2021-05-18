package com.vet24.web;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.CatService;
import com.vet24.service.pet.DogService;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.EchinococcusProcedureService;
import com.vet24.service.pet.procedure.ExternalParasiteProcedureService;
import com.vet24.service.pet.procedure.VaccinationProcedureService;
import com.vet24.service.pet.reproduction.ReproductionService;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.RoleService;
import com.vet24.service.user.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;


@Component
public class TestDataInitializer implements ApplicationRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final ClientService clientService;
    private final MedicineService medicineService;
    private final VaccinationProcedureService vaccinationProcedureService;
    private final ExternalParasiteProcedureService externalParasiteProcedureService;
    private final EchinococcusProcedureService echinococcusProcedureService;
    private final ReproductionService reproductionService;
    private final PetContactService petContactService;
    private final CatService catService;
    private final DogService dogService;
    private final PetService petService;
    private final Environment environment;

    public TestDataInitializer(RoleService roleService, UserService userService,
                               ClientService clientService,
                               MedicineService medicineService, VaccinationProcedureService vaccinationProcedureService,
                               ExternalParasiteProcedureService externalParasiteProcedureService,
                               EchinococcusProcedureService echinococcusProcedureService,
                               ReproductionService reproductionService, PetContactService petContactService,
                               CatService catService, DogService dogService, PetService petService, Environment environment) {
        this.roleService = roleService;
        this.userService = userService;
        this.clientService = clientService;
        this.medicineService = medicineService;
        this.vaccinationProcedureService = vaccinationProcedureService;
        this.externalParasiteProcedureService = externalParasiteProcedureService;
        this.echinococcusProcedureService = echinococcusProcedureService;
        this.reproductionService = reproductionService;
        this.petContactService = petContactService;
        this.catService = catService;
        this.dogService = dogService;
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

    public void medicineInitialize() {
        medicineService.persist(new Medicine("sinopharm", "sputnik", "sdasd",
                "protiv covid"));
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
                LocalDate.now(), LocalDate.now(), LocalDate.now(), 2, petService.getByKey(1L)
        ));
        reproductionService.getByKey(1L);
    }

    public void catInitializer() {
        catService.persist(new Cat("Феликс", LocalDate.now(), Gender.MALE, "Дворовой", clientService.getByKey(3L)));
        catService.persist(new Cat("Тихон", LocalDate.now(), Gender.MALE, "Британский", clientService.getByKey(3L)));
        catService.persist(new Cat("Лаваш", LocalDate.now(), Gender.MALE, "Бенгальский", clientService.getByKey(3L)));
    }

    public void dogInitializer() {
        dogService.persist(new Dog("Жук", LocalDate.now(), Gender.MALE, "Yorkshire Terrier", clientService.getByKey(3L)));
        dogService.persist(new Dog("Туман", LocalDate.now(), Gender.MALE, "Golden Retriever", clientService.getByKey(3L)));
        dogService.persist(new Dog("Рекс", LocalDate.now(), Gender.MALE, "Немецкая овчарка", clientService.getByKey(3L)));
        // для тестирования сохранения в пост методе PetContact контроллера
        dogService.persist(new Dog("Рекс", LocalDate.now(), Gender.MALE, "Немецкая овчарка", clientService.getByKey(3L)));
    }

    public void petContactInitializer() {
        Pet pet1 = petService.getByKey(1L);
        PetContact petContact1 = new PetContact("Екатерина", "Луговое 2", 8_962_987_18_00L, petContactService.randomPetContactUniqueCode(1L));
        petContact1.setPet(pet1);
        petContactService.persist(petContact1);

        Pet pet2 = petService.getByKey(2L);
        PetContact petContact2 = new PetContact("Мария", "Парниковое 7", 8_748_585_55_55L, petContactService.randomPetContactUniqueCode(2L));
        petContact2.setPet(pet2);
        petContactService.persist(petContact2);

        Pet pet3 = petService.getByKey(3L);
        PetContact petContact3 = new PetContact("Олег", "Садовое 27", 8_696_777_42_42L, petContactService.randomPetContactUniqueCode(3L));
        petContact3.setPet(pet3);
        petContactService.persist(petContact3);

        Pet pet4 = petService.getByKey(4L);
        PetContact petContact4 = new PetContact("Дмитрий", "Липовая 3", 8_962_478_02_02L, petContactService.randomPetContactUniqueCode(4L));
        petContact4.setPet(pet4);
        petContactService.persist(petContact4);

        Pet pet5 = petService.getByKey(5L);
        PetContact petContact5 = new PetContact("Кирилл", "Виноградная 20", 8_696_222_322L, petContactService.randomPetContactUniqueCode(5L));
        petContact5.setPet(pet5);
        petContactService.persist(petContact5);

        Pet pet6 = petService.getByKey(6L);
        PetContact petContact6 = new PetContact("Александр", "Стрелковая 70", 8_962_969_103L, petContactService.randomPetContactUniqueCode(6L));
        petContact6.setPet(pet6);
        petContactService.persist(petContact6);
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (Objects.requireNonNull(environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create")
                || Objects.requireNonNull(
                        environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create-drop")) {
            roleInitialize();
            userInitialize();
            medicineInitialize();
            procedureInitializer();
            reproductionInitializer();
            catInitializer();
            dogInitializer();
            petContactInitializer();
        }
    }
}