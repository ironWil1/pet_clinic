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

    public Cat() {
        super();
    }

    public Cat(String name, LocalDate birthDay, Gender gender, String breed, Client client) {
        super(name, birthDay, gender, breed, client);
    }
}