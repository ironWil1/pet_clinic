package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.models.user.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "pet_entities")
@DiscriminatorColumn(name = "pet_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@EqualsAndHashCode
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

    @Column(insertable = false, updatable = false, name = "pet_type")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    protected Pet() {
    }

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed, Client client) {
        this.name = name;
        this.birthDay = birthDay;
        this.gender = gender;
        this.breed = breed;
        this.client = client;
    }
}
