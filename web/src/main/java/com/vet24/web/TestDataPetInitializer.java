package com.vet24.web;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.PetContact;
import com.vet24.service.pet.CatService;
import com.vet24.service.pet.DogService;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.user.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TestDataPetInitializer implements ApplicationRunner {

    private final PetContactService petContactService;
    private final CatService catService;
    private final DogService dogService;
    private final ClientService clientService;

    @Autowired
    Environment environment;

    @Autowired
    public TestDataPetInitializer(PetContactService petContactService, CatService catService, DogService dogService, ClientService clientService) {
        this.petContactService = petContactService;
        this.catService = catService;
        this.dogService = dogService;
        this.clientService = clientService;
    }

    public void petContactInitializer() {
        petContactService.persist(new PetContact("Екатерина", "Луговое 2", 8_962_987_180L, petContactService.randomUniqueCode()));
        petContactService.persist(new PetContact("Олег", "Садовое 27", 8_696_777_424L, petContactService.randomUniqueCode()));
        petContactService.persist(new PetContact("Мария", "Парниковое 7", 8_748_585_555L, petContactService.randomUniqueCode()));
        petContactService.persist(new PetContact("Дмитрий", "Липовая 3", 8_962_478_020L, petContactService.randomUniqueCode()));
        petContactService.persist(new PetContact("Кирилл", "Виноградная 20", 8_696_222_322L, petContactService.randomUniqueCode()));
        petContactService.persist(new PetContact("Александр", "Стрелковая 70", 8_962_969_103L, petContactService.randomUniqueCode()));
    }

    public void catInitializer() {
        catService.persist(new Cat("Феликс", LocalDate.now(), PetType.CAT, Gender.MALE, "Yorkshire Terrier", clientService.getByKey(3L),  petContactService.getByKey(1L)));
        catService.persist(new Cat("Тихон", LocalDate.now(), PetType.CAT, Gender.MALE, "Yorkshire Terrier", clientService.getByKey(3L),  petContactService.getByKey(2L)));
        catService.persist(new Cat("Беляш", LocalDate.now(), PetType.CAT, Gender.MALE, "Yorkshire Terrier", clientService.getByKey(3L),  petContactService.getByKey(3L)));
    }

    public void dogInitializer() {
        dogService.persist(new Dog("Жук", LocalDate.now(), PetType.DOG, Gender.MALE, "Yorkshire Terrier", clientService.getByKey(3L),  petContactService.getByKey(4L)));
        dogService.persist(new Dog("Рекс", LocalDate.now(), PetType.DOG, Gender.MALE, "Golden Retriever", clientService.getByKey(3L),  petContactService.getByKey(5L)));
        dogService.persist(new Dog("Туман", LocalDate.now(), PetType.DOG, Gender.MALE, "Yorkshire Terrier", clientService.getByKey(3L),  petContactService.getByKey(6L)));
    }

    @Override
    public void run(ApplicationArguments args) {
        if (environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create")
                || environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create-drop")) {
            petContactInitializer();
            catInitializer();
            dogInitializer();
        }
    }
}
