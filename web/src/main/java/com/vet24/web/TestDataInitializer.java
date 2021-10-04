package com.vet24.web;

import com.vet24.models.enums.DayOffType;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.*;
import com.vet24.service.medicine.DiagnosisService;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.CatService;
import com.vet24.service.pet.DogService;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.clinicalexamination.ClinicalExaminationService;
import com.vet24.service.pet.procedure.EchinococcusProcedureService;
import com.vet24.service.pet.procedure.ExternalParasiteProcedureService;
import com.vet24.service.pet.procedure.VaccinationProcedureService;
import com.vet24.service.pet.reproduction.ReproductionService;
import com.vet24.service.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


@Component
@Profile("!TestProfile")
public class TestDataInitializer implements ApplicationRunner {

    private final AdminService adminService;
    private final RoleService roleService;
    private final UserService userService;
    private final ClientService clientService;
    private final MedicineService medicineService;
    private final DoctorReviewService doctorReviewService;
    private final VaccinationProcedureService vaccinationProcedureService;
    private final ExternalParasiteProcedureService externalParasiteProcedureService;
    private final EchinococcusProcedureService echinococcusProcedureService;
    private final ReproductionService reproductionService;
    private final ClinicalExaminationService clinicalExaminationService;
    private final PetContactService petContactService;
    private final CatService catService;
    private final DogService dogService;
    private final PetService petService;
    private final DoctorService doctorService;
    private final Environment environment;
    private final CommentService commentService;
    private final CommentReactionService commentReactionService;
    private final DiagnosisService diagnosisService;
    private final TopicService topicService;
    private final DoctorNonWorkingService doctorNonWorkingService;

    private final Role CLIENT = new Role(RoleNameEnum.CLIENT);
    private final Role DOCTOR = new Role(RoleNameEnum.DOCTOR);
    private final Role ADMIN = new Role(RoleNameEnum.ADMIN);
    private final Role MANAGER = new Role(RoleNameEnum.MANAGER);

    private final List<Pet> PETS = new ArrayList<>();
    private final Gender MALE = Gender.MALE;
    private final Gender FEMALE = Gender.FEMALE;
    private final ManagerService managerService;

    @Autowired
    public TestDataInitializer(AdminService adminService,
                               RoleService roleService,
                               UserService userService,
                               ClientService clientService,
                               MedicineService medicineService,
                               VaccinationProcedureService vaccinationProcedureService,
                               ExternalParasiteProcedureService externalParasiteProcedureService,
                               EchinococcusProcedureService echinococcusProcedureService,
                               ReproductionService reproductionService, ClinicalExaminationService clinicalExaminationService, PetContactService petContactService,
                               CatService catService, DogService dogService, DoctorService doctorService,
                               PetService petService, Environment environment, CommentService commentService,
                               CommentReactionService commentReactionService, DiagnosisService diagnosisService,
                               DoctorReviewService doctorReviewService, TopicService topicService, ManagerService managerService,
                               DoctorNonWorkingService doctorNonWorkingService) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.userService = userService;
        this.clientService = clientService;
        this.medicineService = medicineService;
        this.doctorReviewService = doctorReviewService;
        this.vaccinationProcedureService = vaccinationProcedureService;
        this.externalParasiteProcedureService = externalParasiteProcedureService;
        this.echinococcusProcedureService = echinococcusProcedureService;
        this.reproductionService = reproductionService;
        this.clinicalExaminationService = clinicalExaminationService;
        this.petContactService = petContactService;
        this.catService = catService;
        this.dogService = dogService;
        this.petService = petService;
        this.doctorService = doctorService;
        this.environment = environment;
        this.commentService = commentService;
        this.commentReactionService = commentReactionService;
        this.diagnosisService = diagnosisService;
        this.topicService = topicService;
        this.managerService = managerService;
        this.doctorNonWorkingService = doctorNonWorkingService;
    }

    public void roleInitialize() {
        Stream.of(RoleNameEnum.values()).map(Role::new).forEach(roleService::persist);
    }

    public void userInitialize() {
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            clients.add(
                    new Client("ClientFirstName" + i,
                            "ClientLastName" + i,
                            (i == 3) ? "petclinic.vet24@gmail.com" : "client" + i + "@email.com",
                            "client", CLIENT, PETS));
        }
        clientService.persistAll(clients);

        List<Doctor> doctors = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            doctors.add(
                    new Doctor("DoctorFirstName" + i,
                            "DoctorLastName" + i,
                            "doctor" + i + "@email.com",
                            "doctor", DOCTOR));
        }
        doctorService.persistAll(doctors);
    }

    public void adminInit() {
        List<Admin> adminList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            adminList.add(new Admin("AdmFirstName " + i,
                    "AdmLastName " + i,
                    "admin" + i + "@email.com",
                    "admin", ADMIN));
        }
        adminService.persistAll(adminList);
    }

    public void petInitialize() {
        List<Pet> pets = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            if (i <= 15) {
                pets.add(new Dog("DogName" + i, LocalDate.now(), MALE, "DogBreed" + i, clientService.getByKey((long) i)));
            } else {
                pets.add(new Cat("CatName" + i, LocalDate.now(), FEMALE, "CatBreed" + i, clientService.getByKey((long) i)));
            }
        }
        petService.persistAll(pets);
    }

    public void diagnosisInitilaizer() {
        List<Diagnosis> diagnoses = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            diagnoses.add(new Diagnosis(doctorService.getByKey(30 + (long) i), petService.getByKey((long) i), "some diagnosis " + i));
        }
        diagnosisService.persistAll(diagnoses);
    }

    public void medicineInitialize() {
        List<Medicine> medicines = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            medicines.add(new Medicine("manufactureName" + i, "name" + i, "icon" + i, "description" + i));
        }
        medicineService.persistAll(medicines);
    }

    public void procedureInitializer() {
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
                        true, i, medicineService.getByKey((long) i), petService.getByKey((long) i)));
            }
            if (i > 20) {
                echinococcus.add(new EchinococcusProcedure(LocalDate.now(), "EchinococcusMedicineBatchNumber" + i,
                        true, i, medicineService.getByKey((long) i), petService.getByKey((long) i)));
            }
        }
        vaccinationProcedureService.persistAll(vaccination);
        externalParasiteProcedureService.persistAll(externalParasite);
        echinococcusProcedureService.persistAll(echinococcus);
    }

    public void reproductionInitializer() {
        List<Reproduction> reproductions = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            reproductions.add(new Reproduction(LocalDate.now(), LocalDate.now(), LocalDate.now(), i, petService.getByKey((long) i)));
        }
        reproductionService.persistAll(reproductions);
    }

    //clinical examination
    public void clinicalExaminationInitializer() {
        List<ClinicalExamination> clinicalExaminations = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            clinicalExaminations.add(new ClinicalExamination(LocalDate.now(), petService.getByKey((long) i), doctorService.getByKey((long) i + 30), (double) i, true, "text"));
        }
        clinicalExaminationService.persistAll(clinicalExaminations);
    }


    public void petContactInitializer() {
        Pet pet1 = petService.getByKey(1L);
        PetContact petContact1 = new PetContact("Екатерина", "Луговое 2", 8_962_987_18_00L, petContactService.randomPetContactUniqueCode());
        petContact1.setPet(pet1);
        petContactService.persist(petContact1);

        Pet pet2 = petService.getByKey(2L);
        PetContact petContact2 = new PetContact("Мария", "Парниковое 7", 8_748_585_55_55L, petContactService.randomPetContactUniqueCode());
        petContact2.setPet(pet2);
        petContactService.persist(petContact2);

        Pet pet3 = petService.getByKey(3L);
        PetContact petContact3 = new PetContact("Олег", "Садовое 27", 8_696_777_42_42L, petContactService.randomPetContactUniqueCode());
        petContact3.setPet(pet3);
        petContactService.persist(petContact3);

        Pet pet4 = petService.getByKey(4L);
        PetContact petContact4 = new PetContact("Дмитрий", "Липовая 3", 8_962_478_02_02L, petContactService.randomPetContactUniqueCode());
        petContact4.setPet(pet4);
        petContactService.persist(petContact4);

        Pet pet5 = petService.getByKey(5L);
        PetContact petContact5 = new PetContact("Кирилл", "Виноградная 20", 8_696_222_32_23L, petContactService.randomPetContactUniqueCode());
        petContact5.setPet(pet5);
        petContactService.persist(petContact5);

        Pet pet6 = petService.getByKey(6L);
        PetContact petContact6 = new PetContact("Александр", "Стрелковая 70", 8_962_969_10_30L, petContactService.randomPetContactUniqueCode());
        petContact6.setPet(pet6);
        petContactService.persist(petContact6);
    }

    public void commentInitializer() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            comments.add(new Comment(userService.getByKey((long) i), "lorem " + i));
        }
        commentService.persistAll(comments);

    }

    public void likeInitilaizer() {
        for (int i = 1; i <= 30; i++) {
            commentReactionService.update(new CommentReaction(commentService.getByKey((long) i), clientService.getByKey((long) i), true));
        }
    }

    public void doctorReviewInitializer() {
        List<DoctorReview> doctorReviews = new ArrayList<>();
        Comment doctorReviewComment = null;
        for (int i = 1; i <= 30; i++) {
            doctorReviewComment = new Comment(userService.getByKey((long) i + 30), "lorem " + (i + 30), LocalDateTime.now());
            commentService.persist(doctorReviewComment);
            doctorReviews.add(new DoctorReview(doctorReviewComment, doctorService.getByKey((long) i + 30)));
        }
        doctorReviewService.persistAll(doctorReviews);
    }

    public void topicInitializer() {
        List<Topic> listTopic = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            List<Comment> commentList = new ArrayList<>();
            commentList.add(new Comment(userService.getByKey((long) i + 30), "comment for topic " + (i + 30), LocalDateTime.now()));
            commentList.add(new Comment(userService.getByKey((long) i + 30), "comment for topic " + (i + 30), LocalDateTime.now()));
            commentService.persistAll(commentList);
            listTopic.add(new Topic(userService.getByKey((long) i), "topic" + i, "content" + i, false, commentList));
        }
        topicService.persistAll(listTopic);
    }

    private void managerInit() {
        List<Manager> managerList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            managerList.add(new Manager("ManagerFirstName " + i,
                    "ManagerLastName " + i,
                    "manager" + i + "@email.com",
                    "manager", MANAGER));
        }
        managerService.persistAll(managerList);
    }

    public void doctorNonWorkingInit() {
        List<DoctorNonWorking> doctorNonWorkings = Arrays.asList(
                new DoctorNonWorking(doctorService.getByKey(31L), DayOffType.SICK_LEAVE, LocalDate.of(2021, 10, 15)),
                new DoctorNonWorking(doctorService.getByKey(32L), DayOffType.DAY_OFF, LocalDate.of(2021, 10, 10)),
                new DoctorNonWorking(doctorService.getByKey(33L), DayOffType.VACATION, LocalDate.of(2021, 10, 7))
        );
        doctorNonWorkingService.persistAll(doctorNonWorkings);
    }

//    public void topicInitializer() {
//        List<Topic> listTopic = new ArrayList<>();
//        List<Comment> commentList = new ArrayList<>();
//        for (int i = 1; i <= 30; i++) {
//            commentList.add(new Comment(userService.getByKey((long) i + 30), "comment for topic " + (i + 30), LocalDateTime.now()));
//            listTopic.add(new Topic(userService.getByKey((long)i),"topic" + i, "content" + i, false, commentList));
//        }
//        topicService.persistAll(listTopic);
//    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (Objects.requireNonNull(environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create")
                || Objects.requireNonNull(
                environment.getProperty("spring.jpa.hibernate.ddl-auto")).equals("create-drop")) {
            roleInitialize();
            userInitialize();
            petInitialize();
            diagnosisInitilaizer();
            medicineInitialize();
            procedureInitializer();
            reproductionInitializer();
            clinicalExaminationInitializer();
            petContactInitializer();
            commentInitializer();
            likeInitilaizer();
            topicInitializer();
            doctorReviewInitializer();
            adminInit();
            managerInit();
            doctorNonWorkingInit();
        }
    }
}