package com.vet24.web;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
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

    @Autowired
    Environment environment;

    @Autowired
    public TestDataPetInitializer(PetContactService petContactService, PetService petService) {
        this.petContactService = petContactService;
        this.petService = petService;
    }

    public void petContactInitialize() {
        petContactService.persist(new PetContact("Ольга", "Луговое 2", 8696585968L, 869568589589849L));
        petContactService.persist(new PetContact("Олег", "Садовое 27", 8696585968L, 234456576788877L));
        petContactService.persist(new PetContact("Мария", "Парниковое 7", 8696585968L, 46547657689898L));
    }

    public void petInitialize() {
        petService.persist(new Cat("Felix", petContactService.getByKey(1L)));
        petService.persist(new Cat("Murzik", petContactService.getByKey(2L)));
        petService.persist(new Cat("Beljach", petContactService.getByKey(3L)));
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
