package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.models.user.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "pet_entities")
@DiscriminatorColumn(name = "pet_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed, Client client) {
        this.name = name;
        this.birthDay = birthDay;
        this.gender = gender;
        this.breed = breed;
        this.client = client;
    }
}
