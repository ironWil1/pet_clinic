package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.models.medicine.Appointment;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.models.pet.procedure.Deworming;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "pet_entities")
@DiscriminatorColumn(name = "pet_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
    private List<Procedure> procedures = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ExternalParasiteProcedure> externalParasiteProcedures = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Deworming> dewormings = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Diagnosis> diagnoses = new ArrayList<>();


    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reproduction> reproductions = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PetFound> petFounds = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ClinicalExamination> clinicalExaminations = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Appointment> appointments = new ArrayList<>();

    @OneToOne(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private PetContact petContact;

    protected Pet() {
    }

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed, Client client) {
        this.name = name;
        this.birthDay = birthDay;
        this.gender = gender;
        this.breed = breed;
        this.client = client;
    }

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed, Client client,
                  List<Procedure> procedures, List<ExternalParasiteProcedure> externalParasiteProcedures,
                  List<Deworming> dewormings, List<Reproduction> reproductions,
                  List<ClinicalExamination> clinicalExaminations) {
        this(name, birthDay, gender, breed, client);
        this.procedures = procedures;
        this.externalParasiteProcedures = externalParasiteProcedures;
        this.dewormings = dewormings;
        this.reproductions = reproductions;
        this.clinicalExaminations = clinicalExaminations;
    }

    public void addProcedure(Procedure procedure) {
        procedures.add(procedure);
        procedure.setPet(this);
    }

    public void addExternalParasiteProcedure(ExternalParasiteProcedure externalParasiteProcedure) {
        externalParasiteProcedures.add(externalParasiteProcedure);
        externalParasiteProcedure.setPet(this);
    }

    public void removeProcedure(Procedure procedure) {
        procedures.remove(procedure);
        procedure.setPet(null);
    }

    public void removeExternalParasiteProcedure(ExternalParasiteProcedure externalParasiteProcedure) {
        externalParasiteProcedures.remove(externalParasiteProcedure);
        externalParasiteProcedure.setPet(null);
    }

    public void addReproduction(Reproduction reproduction) {
        reproductions.add(reproduction);
        reproduction.setPet(this);
    }

    public void removeReproduction(Reproduction reproduction) {
        reproductions.remove(reproduction);
        reproduction.setPet(null);
    }

    public void addClinicalExamination(ClinicalExamination clinicalExamination) {
        clinicalExaminations.add(clinicalExamination);
        clinicalExamination.setPet(this);
    }

    public void removeClinicalExamination(ClinicalExamination clinicalExamination) {
        clinicalExaminations.remove(clinicalExamination);
        clinicalExamination.setPet(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Pet pet = (Pet) o;
        return id != null && Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
