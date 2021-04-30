package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "pet_entities")
@DiscriminatorColumn(name = "pet_discriminator", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String avatar;

    @Column(nullable = false)
    private LocalDate birthDay;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String breed;

    @Column
    private String color;

    @Column
    @Enumerated(EnumType.STRING)
    private PetSize petSize;

    @Column
    private Double weight;

    @Column
    private String description;

    public Pet() {
    }

    public Pet(String name, LocalDate birthDay, PetType petType, Gender gender, String breed) {
        this.name = name;
        this.birthDay = birthDay;
        this.petType = petType;
        this.gender = gender;
        this.breed = breed;
    }
}
