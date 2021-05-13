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

    public Dog() {
        super();
    }

    public Dog(String petName, PetContact petContact) {
        super(petName, petContact);
    }

    public Dog(String petName, LocalDate birthDay, Gender gender, String breed, Client client, PetContact petContact) {
        super(petName, birthDay, gender, breed, client, petContact);
    }
}
