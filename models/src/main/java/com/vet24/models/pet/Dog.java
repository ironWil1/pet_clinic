package com.vet24.models.pet;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetType;
import com.vet24.models.user.Client;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("DOG")
public class Dog extends Pet{

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private PetContact petContact;

    public Dog() {
        super();
    }

    public Dog(String petName, LocalDate birthDay, PetType petType, Gender gender, String breed, Client client, PetContact petContact) {
        super(petName, birthDay, PetType.DOG, gender, breed, client);
        this.petContact = petContact;
    }

    // add later when ready

    //    List<Vaccinations> vaccinations;
    //    List<echinococcus> echinococcus;
    //    List<externalParasite> externalParasite;
    //    List<Reproduction> reproduction;

}
