package com.vet24.web;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.service.pet.CatService;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class TestDataPetInitializer implements ApplicationRunner {

    private final PetContactService petContactService;
    private final PetService petService;
    private final CatService catService;

    @Autowired
    Environment environment;

    @Autowired
    public TestDataPetInitializer(PetContactService petContactService, PetService petService, CatService catService) {
        this.petContactService = petContactService;
        this.petService = petService;
        this.catService = catService;
    }

    public void petContactInitialize() {
        petContactService.persist(new PetContact("Екатерина", "Луговое 2", 8_962_987_180L, petContactService.randomUniqueCode()));
        petContactService.persist(new PetContact("Олег", "Садовое 27", 8_696_777_424L, petContactService.randomUniqueCode()));
        petContactService.persist(new PetContact("Мария", "Парниковое 7", 8_748_585_555L, petContactService.randomUniqueCode()));
    }

    public void petInitialize() {
        catService.persist(new Cat("Феликс", petContactService.getByKey(1L)));
        catService.persist(new Cat("Тихон", petContactService.getByKey(2L)));
        catService.persist(new Cat("Беляш", petContactService.getByKey(3L)));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create")
                || environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create-drop")) {
            petContactInitialize();
            petInitialize();
        }
    }
}
