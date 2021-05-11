package com.vet24.models.pet;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Data
public abstract class Pet {

    @Id
    //@GeneratedValue(strategy = GenerationType.TABLE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String petName = "Мурзик";

    @OneToOne(fetch = FetchType.EAGER)
    private PetContact petContact;
}
