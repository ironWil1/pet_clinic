package com.vet24.web;

import com.vet24.models.enums.*;
import com.vet24.models.medicine.Appointment;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.news.News;
import com.vet24.models.notification.Notification;
import com.vet24.models.notification.UserNotification;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.PetFound;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.models.pet.procedure.Deworming;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.Comment;
import com.vet24.models.user.CommentReaction;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.models.user.DoctorReview;
import com.vet24.models.user.Role;
import com.vet24.models.user.Topic;
import com.vet24.models.user.User;
import com.vet24.service.medicine.AppointmentService;
import com.vet24.service.medicine.DiagnosisService;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.news.NewsService;
import com.vet24.service.notification.NotificationService;
import com.vet24.service.notification.UserNotificationService;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetFoundService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.clinicalexamination.ClinicalExaminationService;
import com.vet24.service.pet.procedure.DewormingService;
import com.vet24.service.pet.procedure.ExternalParasiteProcedureService;
import com.vet24.service.pet.procedure.VaccinationProcedureService;
import com.vet24.service.pet.reproduction.ReproductionService;
import com.vet24.service.user.CommentReactionService;
import com.vet24.service.user.CommentService;
import com.vet24.service.user.DoctorNonWorkingService;
import com.vet24.service.user.DoctorReviewService;
import com.vet24.service.user.ProfileService;
import com.vet24.service.user.RoleService;
import com.vet24.service.user.TopicService;
import com.vet24.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@Profile({"local", "prod"})
public class TestDataInitializer implements ApplicationRunner {

    private static final WorkShift firstShift = WorkShift.FIRST_SHIFT;
    private static final WorkShift secondShift = WorkShift.SECOND_SHIFT;
    private static final Gender MALE = Gender.MALE;
    private static final Gender FEMALE = Gender.FEMALE;
    private static final String EMAIL = "@email.com";
    private final RoleService roleService;
    private final UserService userService;
    private final MedicineService medicineService;
    private final DoctorReviewService doctorReviewService;
    private final VaccinationProcedureService vaccinationProcedureService;
    private final ExternalParasiteProcedureService externalParasiteProcedureService;
    private final DewormingService dewormingService;
    private final ReproductionService reproductionService;
    private final ClinicalExaminationService clinicalExaminationService;
    private final PetContactService petContactService;
    private final PetService petService;
    private final Environment environment;
    private final CommentService commentService;
    private final CommentReactionService commentReactionService;
    private final DiagnosisService diagnosisService;
    private final TopicService topicService;
    private final DoctorNonWorkingService doctorNonWorkingService;
    private final AppointmentService appointmentService;
    private final DoctorScheduleService doctorScheduleService;
    private final NotificationService notificationService;
    private final UserNotificationService userNotificationService;
    private final NewsService newsService;
    private final ProfileService profileService;
    private final Role client = new Role(RoleNameEnum.CLIENT);
    private final Role doctor = new Role(RoleNameEnum.DOCTOR);
    private final Role admin = new Role(RoleNameEnum.ADMIN);
    private final Role manager = new Role(RoleNameEnum.MANAGER);
    private final List<Pet> petList = new ArrayList<>();
    private final PetFoundService petFoundService;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TestDataInitializer(
            RoleService roleService,
            UserService userService,
            MedicineService medicineService,
            VaccinationProcedureService vaccinationProcedureService,
            ExternalParasiteProcedureService externalParasiteProcedureService,
            DewormingService dewormingService,
            ReproductionService reproductionService, ClinicalExaminationService clinicalExaminationService, PetContactService petContactService,
            PetService petService, DoctorScheduleService doctorScheduleService, Environment environment, CommentService commentService,
            CommentReactionService commentReactionService, DiagnosisService diagnosisService,
            DoctorReviewService doctorReviewService, TopicService topicService,
            DoctorNonWorkingService doctorNonWorkingService, AppointmentService appointmentService, NotificationService notificationService,
            UserNotificationService userNotificationService, NewsService newsService, ProfileService profileService, PetFoundService petFoundService) {

        this.roleService = roleService;
        this.userService = userService;
        this.medicineService = medicineService;
        this.doctorScheduleService = doctorScheduleService;
        this.doctorReviewService = doctorReviewService;
        this.vaccinationProcedureService = vaccinationProcedureService;
        this.externalParasiteProcedureService = externalParasiteProcedureService;
        this.dewormingService = dewormingService;
        this.reproductionService = reproductionService;
        this.clinicalExaminationService = clinicalExaminationService;
        this.petContactService = petContactService;
        this.petService = petService;
        this.environment = environment;
        this.commentService = commentService;
        this.commentReactionService = commentReactionService;
        this.diagnosisService = diagnosisService;
        this.topicService = topicService;
        this.doctorNonWorkingService = doctorNonWorkingService;
        this.appointmentService = appointmentService;
        this.notificationService = notificationService;
        this.userNotificationService = userNotificationService;
        this.newsService = newsService;

        this.profileService = profileService;
        this.petFoundService = petFoundService;
    }

    public void roleInitialize() {
        Stream.of(RoleNameEnum.values()).map(Role::new).forEach(roleService::persist);
    }

    public void userInitialize() {

        List<User> clients = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            clients.add(
                    new User((i == 3) ? "petclinic.vet24@gmail.com" : "client" + i + EMAIL,
                            "client", client, petList));
        }
        userService.persistAll(clients);

        List<User> doctors = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            doctors.add(
                    new User("doctor" + i + EMAIL,
                            "doctor", doctor));
        }
        userService.persistAll(doctors);
    }

    public void adminInit() {
        List<User> adminList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            adminList.add(new User("admin" + i + EMAIL,
                    "admin", admin));
        }
        userService.persistAll(adminList);
    }

    public void petInitialize() {
        List<Pet> pets = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            if (i <= 15) {
                pets.add(new Pet("DogName" + i, LocalDate.now(), PetType.DOG, MALE, "DogBreed" + i, userService.getByKey((long) i)));
            } else {
                pets.add(new Pet("CatName" + i, LocalDate.now(), PetType.CAT, FEMALE, "CatBreed" + i, userService.getByKey((long) i)));
            }
        }
        petService.persistAll(pets);
    }

    public void diagnosisInitilaizer() {
        List<Diagnosis> diagnoses = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            diagnoses.add(new Diagnosis(userService.getByKey(30 + (long) i), petService.getByKey((long) i), "some diagnosis " + i));
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

    public void externalParasiteInitializer() {
        List<ExternalParasiteProcedure> externalParasiteProcedures = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            externalParasiteProcedures.add(new ExternalParasiteProcedure(LocalDate.now(), "ExternalParasiteMedicineBatchNumber" + i,
                    true, 2, medicineService.getByKey((long) i), petService.getByKey((long) i)));
            externalParasiteProcedures.add(new ExternalParasiteProcedure(LocalDate.now().plusDays(2), "ExternalParasiteMedicineBatchNumber" + i,
                    true, 4, medicineService.getByKey((long) i), petService.getByKey((long) i)));
            externalParasiteProcedures.add(new ExternalParasiteProcedure(LocalDate.now().plusDays(6), "ExternalParasiteMedicineBatchNumber" + i,
                    false, 0, medicineService.getByKey((long) i), petService.getByKey((long) i)));
        }
        externalParasiteProcedureService.persistAll(externalParasiteProcedures);
    }

    public void dewormingInitializer() {
        List<Deworming> dewormings = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            dewormings.add(new Deworming(LocalDate.now(), "DewormingMedicineBatchNumber" + i,
                    true, 2, medicineService.getByKey((long) i), petService.getByKey((long) i)));
            dewormings.add(new Deworming(LocalDate.now().plusDays(2), "DewormingMedicineBatchNumber" + i,
                    true, 4, medicineService.getByKey((long) i), petService.getByKey((long) i)));
            dewormings.add(new Deworming(LocalDate.now().plusDays(6), "DewormingMedicineBatchNumber" + i,
                    false, 0, medicineService.getByKey((long) i), petService.getByKey((long) i)));
        }
        dewormingService.persistAll(dewormings);
    }

    public void vaccinationInitializer() {
        List<VaccinationProcedure> vaccinations = new ArrayList<>();
        for (int petId = 1; petId <= 30; petId++) {
            for (int procedureId = 1; procedureId <= 3; procedureId++) {
                vaccinations.add(new VaccinationProcedure(LocalDate.now(), "VaccinationMedicineBatchNumber" + procedureId,
                        false, 0, medicineService.getByKey((long) petId), petService.getByKey((long) petId)));
            }
        }
        vaccinationProcedureService.persistAll(vaccinations);
    }

//    public void procedureInitializer() {
//        List<VaccinationProcedure> vaccination = new ArrayList<>();
//        List<ExternalParasiteProcedure> externalParasite = new ArrayList<>();
//        List<EchinococcusProcedure> echinococcus = new ArrayList<>();
//
//        for (int i = 1; i <= 30; i++) {
//            if (i <= 10) {
//                vaccination.add(new VaccinationProcedure(LocalDate.now(), "VaccinationMedicineBatchNumber" + i,
//                        false, i, medicineService.getByKey((long) i), petService.getByKey((long) i)));
//            }
//            if (i > 10 && i <= 20) {
//                externalParasite.add(new ExternalParasiteProcedure(LocalDate.now(), "ExternalParasiteMedicineBatchNumber" + i,
//                        true, i, medicineService.getByKey((long) i), petService.getByKey((long) i)));
//            }
//            if (i > 20) {
//                echinococcus.add(new EchinococcusProcedure(LocalDate.now(), "EchinococcusMedicineBatchNumber" + i,
//                        true, i, medicineService.getByKey((long) i), petService.getByKey((long) i)));
//            }
//        }
//        vaccinationProcedureService.persistAll(vaccination);
//        externalParasiteProcedureService.persistAll(externalParasite);
//        echinococcusProcedureService.persistAll(echinococcus);
//    }

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
            clinicalExaminations.add(new ClinicalExamination(LocalDate.now(), petService.getByKey((long) i), userService.getByKey((long) i + 30), (double) i, true, "text"));
        }
        clinicalExaminationService.persistAll(clinicalExaminations);
    }

    public void petContactInitializer() {
        Pet pet1 = petService.getByKey(1L);
        PetContact petContact1 = new PetContact("Екатерина", "Луговое 2", 8_962_987_18_00L, "description", petContactService.randomPetContactUniqueCode());
        petContact1.setPet(pet1);
        petContactService.persist(petContact1);

        Pet pet2 = petService.getByKey(2L);
        PetContact petContact2 = new PetContact("Мария", "Парниковое 7", 8_748_585_55_55L, "description", petContactService.randomPetContactUniqueCode());
        petContact2.setPet(pet2);
        petContactService.persist(petContact2);

        Pet pet3 = petService.getByKey(3L);
        PetContact petContact3 = new PetContact("Олег", "Садовое 27", 8_696_777_42_42L, "description", petContactService.randomPetContactUniqueCode());
        petContact3.setPet(pet3);
        petContactService.persist(petContact3);

        Pet pet4 = petService.getByKey(4L);
        PetContact petContact4 = new PetContact("Дмитрий", "Липовая 3", 8_962_478_02_02L, "description", petContactService.randomPetContactUniqueCode());
        petContact4.setPet(pet4);
        petContactService.persist(petContact4);

        Pet pet5 = petService.getByKey(5L);
        PetContact petContact5 = new PetContact("Кирилл", "Виноградная 20", 8_696_222_32_23L, "description", petContactService.randomPetContactUniqueCode());
        petContact5.setPet(pet5);
        petContactService.persist(petContact5);

        Pet pet6 = petService.getByKey(6L);
        PetContact petContact6 = new PetContact("Александр", "Стрелковая 70", 8_962_969_10_30L, "description", petContactService.randomPetContactUniqueCode());
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
            commentReactionService.update(new CommentReaction(commentService.getByKey((long) i), userService.getByKey((long) i), true));
        }
    }

    public void doctorReviewInitializer() {
        List<DoctorReview> doctorReviews = new ArrayList<>();
        Comment doctorReviewComment;
        for (int i = 1; i <= 30; i++) {
            for (int j = 1; j <= 30; j++) {
                doctorReviewComment = new Comment(userService.getByKey((long) j), "комментарий пользователя " + j + " доктору " + (i + 30), LocalDateTime.now());
                doctorReviews.add(new DoctorReview(doctorReviewComment, userService.getByKey((long) i + 30)));
            }
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
        List<User> managerList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            managerList.add(new User("manager" + i + EMAIL,
                    "manager", manager));
        }
        userService.persistAll(managerList);
    }

    private void doctorScheduleInit() {
        List<DoctorSchedule> doctorScheduleList = new ArrayList<>();
        doctorScheduleList.add(new DoctorSchedule(userService.getByKey(31L), firstShift, LocalDate.of(2021, 7, 26)));
        doctorScheduleList.add(new DoctorSchedule(userService.getByKey(32L), secondShift, LocalDate.of(2021, 7, 26)));
        doctorScheduleList.add(new DoctorSchedule(userService.getByKey(33L), secondShift, LocalDate.of(2021, 7, 26)));
        doctorScheduleService.persistAll(doctorScheduleList);
    }

    public void doctorNonWorkingInit() {
        List<DoctorNonWorking> doctorNonWorkings = Arrays.asList(
                new DoctorNonWorking(userService.getByKey(31L), DayOffType.SICK_LEAVE, LocalDate.of(2021, 10, 15)),
                new DoctorNonWorking(userService.getByKey(32L), DayOffType.DAY_OFF, LocalDate.of(2021, 10, 10)),
                new DoctorNonWorking(userService.getByKey(33L), DayOffType.VACATION, LocalDate.of(2021, 10, 7))
        );
        doctorNonWorkingService.persistAll(doctorNonWorkings);
    }

    public void appointmentInit() {
        List<Appointment> appointmentList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            appointmentList.add(new Appointment(userService.getByKey((long) i), petService.getByKey((long) i), LocalDateTime.now().plusDays(7), "description" + i));
        }
        appointmentService.persistAll(appointmentList);
    }

    public void notificationAndUserNotificationInit() {
        Notification clientNotification = new Notification("Тестовое уведомление CLIENT", LocalDate.now().plusDays(1), true);
        Notification doctorNotification = new Notification("Тестовое уведомление DOCTOR", LocalDate.now().plusDays(5), true);
        Notification adminNotification = new Notification("Тестовое уведомление ADMIN", LocalDate.now().plusDays(7), true);
        Notification managerNotification = new Notification("Тестовое уведомление MANAGER", LocalDate.now().plusDays(4), true);

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(clientNotification);
        notificationList.add(doctorNotification);
        notificationList.add(adminNotification);
        notificationList.add(managerNotification);

        List<UserNotification> userNotificationList = new ArrayList<>();
        userNotificationList.add(new UserNotification(clientNotification, userService.getByKey(1L), false));
        userNotificationList.add(new UserNotification(doctorNotification, userService.getByKey(31L), false));
        userNotificationList.add(new UserNotification(adminNotification, userService.getByKey(61L), false));
        userNotificationList.add(new UserNotification(managerNotification, userService.getByKey(66L), false));

        notificationService.persistAll(notificationList);
        userNotificationService.persistAll(userNotificationList);
    }

    public void newsInit() {
        List<News> newsList = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {

            if (i <= 10) {
                News updatingNews = new News();
                updatingNews.setContent("Content of Updating" + i);
                updatingNews.setImportant(true);
                updatingNews.setEndTime(LocalDateTime.now().plusDays(i));
                updatingNews.setType(NewsType.UPDATING);
                newsList.add(updatingNews);

            }

            if (i > 10 && i <= 20) {
                News advertisingActionsNews = new News();
                advertisingActionsNews.setContent("Content of Advertising Actions" + i);
                advertisingActionsNews.setImportant(false);
                advertisingActionsNews.setEndTime(LocalDateTime.now().plusWeeks(i));
                advertisingActionsNews.setType(NewsType.ADVERTISING_ACTIONS);
                newsList.add(advertisingActionsNews);
            }

            if (i > 20 && i <= 30) {
                News discountsNews = new News();
                discountsNews.setContent("Content of Discounts News" + i);
                discountsNews.setImportant(true);
                discountsNews.setEndTime(LocalDateTime.now().plusDays(i));
                discountsNews.setType(NewsType.DISCOUNTS);
                newsList.add(discountsNews);
            }

            if (i > 30) {
                News promotionNews = new News();
                promotionNews.setContent("Content of Promotion News" + i);
                promotionNews.setImportant(false);
                promotionNews.setEndTime(LocalDateTime.now().plusWeeks(i));
                promotionNews.setType(NewsType.PROMOTION);
                newsList.add(promotionNews);
            }
        }
        newsService.persistAll(newsList);
    }

    public void profileInit() {
        List<User> users = userService.getAll();
        List<com.vet24.models.user.Profile> profileList = new ArrayList<>();
        for (int i = 1; i <= users.size(); i++) {
            com.vet24.models.user.Profile profile = new com.vet24.models.user.Profile();
            profile.setUser(users.get(i - 1));
            profile.setAvatarUrl("avatarUrl" + i);
            profile.setFirstName("firstName" + i);
            profile.setLastName("lastName" + i);
            profile.setDiscordId("discordId" + i);
            profile.setTelegramId("telegramId" + i);
            profile.setBirthDate(LocalDate.parse("1970-01-01"));
            profile.setDiscordNotify(false);
            profile.setEmailNotify(false);
            profileList.add(profile);
        }
        profileService.persistAll(profileList);
    }


    public void petFoundInit() {
        List<Pet> pets = petService.getAll();
        List<PetFound> petFoundList = new ArrayList<>();
        for (int i = 1; i <= pets.size(); i++) {
            for (int j = 1; j <= 5; j++) {
                PetFound petFoundTest = new PetFound();
                petFoundTest.setFoundDate(LocalDateTime.now());
                petFoundTest.setMessage("сообщение");
                petFoundTest.setLongitude("долгота");
                petFoundTest.setLatitude("широта");
                petFoundTest.setPet(petService.getByKey((long) i));
                petFoundList.add(petFoundTest);
            }
        }
        petFoundService.persistAll(petFoundList);
    }

    public void appearanceInit() {
        String[] catBreeds = {"bengal", "bombay", "maine coon", "persian", "munchkin"};
        String[] dogBreeds = {"boxer", "labrador", "poodle", "bulldog", "pug"};
        String[] colors = {"black", "white", "orange", "brown", "yellow"};

        String petSQL = "INSERT INTO pet_breed (pet_type, breed) VALUES (:petType, :breed) ON CONFLICT (pet_type, breed) DO NOTHING;";
        String colorSQL = "INSERT INTO pet_color (color) VALUES (:color) ON CONFLICT (color) DO NOTHING;";

        for (int i = 0; i < 5; i++) {
            entityManager.createNativeQuery(petSQL)
                    .setParameter("petType", "CAT").setParameter("breed", catBreeds[i])
                    .executeUpdate();
            entityManager.createNativeQuery(petSQL)
                    .setParameter("petType", "DOG").setParameter("breed", dogBreeds[i])
                    .executeUpdate();
            entityManager.createNativeQuery(colorSQL)
                    .setParameter("color", colors[i]).executeUpdate();
        }
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (Objects.requireNonNull(environment.getProperty("createTestData")).equals("true")) {
            roleInitialize();
            userInitialize();
            petInitialize();
            diagnosisInitilaizer();
            medicineInitialize();
            externalParasiteInitializer();
            dewormingInitializer();
            vaccinationInitializer();
            reproductionInitializer();
            clinicalExaminationInitializer();
            petContactInitializer();
            commentInitializer();
            likeInitilaizer();
            topicInitializer();
            doctorReviewInitializer();
            adminInit();
            managerInit();
            doctorScheduleInit();
            doctorNonWorkingInit();
            appointmentInit();
            notificationAndUserNotificationInit();
            newsInit();
            profileInit();
            petFoundInit();
            appearanceInit();
        }
    }
}
