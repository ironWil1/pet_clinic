package com.vet24.models.pet;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Cat extends Pet{

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private PetContact petContact;

    public Cat() {
        super();
    }

    public Cat(String petName, PetContact petContact) {
        super(petName);
        this.petContact = petContact;
    }
}
