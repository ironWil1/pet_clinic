package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetType;
import com.vet24.models.user.Client;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("CAT")
public class Cat extends Pet {

    public Cat() {
        super();
    }

    public Cat(String petName, PetContact petContact) {
        super(petName, petContact);
    }

    public Cat(String petName, LocalDate birthDay, PetType petType, Gender gender, String breed, Client client, PetContact petContact) {
        super(petName, birthDay, PetType.CAT, gender, breed, client, petContact);
    }
}
