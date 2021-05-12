package com.vet24.models.pet;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDate;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetType;
import com.vet24.models.user.Client;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("CAT")
public class Cat extends Pet {

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private PetContact petContact;

    public Cat() {
        super();
    }

    public Cat(String petName, PetContact petContact) {
        super(petName);
        this.petContact = petContact;
    }

    public Cat(String petName, LocalDate birthDay, PetType petType, Gender gender, String breed, Client client, PetContact petContact) {
        super(petName, birthDay, PetType.CAT, gender, breed, client);
        this.petContact = petContact;
    }
}
