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
@DiscriminatorValue("CAT")
@Getter
@Setter
public class Cat extends Pet {

    private PetType petType;

    public Cat() {
        super();
        this.petType = PetType.CAT;
    }

    public Cat(String petName, LocalDate birthDay, Gender gender, String breed, Client client) {
        super(petName, birthDay, gender, breed, client);
        this.petType = PetType.CAT;
    }
}