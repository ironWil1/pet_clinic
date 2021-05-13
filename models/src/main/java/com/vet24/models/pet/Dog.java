package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetType;
import com.vet24.models.user.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("DOG")
@Getter
@Setter
public class Dog extends Pet {

    private PetType petType;

    public Dog() {
        super();
    }

    public Dog(String name, LocalDate birthDay, Gender gender, String breed, Client client) {
        super(name, birthDay, gender, breed, client);
        this.petType = PetType.DOG;
    }

    // add later when ready

    //    List<Vaccinations> vaccinations;
    //    List<echinococcus> echinococcus;
    //    List<externalParasite> externalParasite;
    //    List<Reproduction> reproduction;

}
