package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Procedure> procedures = new HashSet<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Reproduction> reproductions = new HashSet<>();

    protected Pet() {
    }

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed, Client client) {
        this.name = name;
        this.birthDay = birthDay;
        this.gender = gender;
        this.breed = breed;
        this.client = client;
    }

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed, Client client, Set<Procedure> procedures) {
        this(name, birthDay, gender, breed, client);
        this.procedures = procedures;
    }

    public void addProcedure(Procedure procedure) {
        procedures.add(procedure);
        procedure.setPet(this);
    }

    public void removeProcedure(Procedure procedure) {
        procedures.remove(procedure);
        procedure.setPet(null);
    }

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed,
                  Client client, Set<Reproduction> reproductions) {
        this(name, birthDay, gender, breed, client);
        this.reproductions = reproductions;
    }

    public void addReproduction(Reproduction reproduction){
        reproductions.add(reproduction);
        reproduction.setPet(this);
    }

    public void removeReproduction(Reproduction reproduction) {
        reproductions.remove(reproduction);
        reproduction.setPet(null);
    }
}
