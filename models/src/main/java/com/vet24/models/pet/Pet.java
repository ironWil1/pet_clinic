package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.models.medicine.Appointment;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.models.pet.procedure.Deworming;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pet_entities")
@Getter
@Setter
public class Pet {

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

    @ManyToOne(fetch = FetchType.LAZY)
    private User client;

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
    private List<VaccinationProcedure> vaccinationProcedures = new ArrayList<>();

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

    public Pet() {
    }

    public Pet(String name, LocalDate birthDay, PetType petType, Gender gender, String breed, User client) {
        this.name = name;
        this.birthDay = birthDay;
        this.petType = petType;
        this.gender = gender;
        this.breed = breed;
        this.client = client;
    }

    public Pet(String name, LocalDate birthDay, PetType petType, Gender gender, String breed, User client,
                  List<ExternalParasiteProcedure> externalParasiteProcedures,
                  List<Deworming> dewormings, List<Reproduction> reproductions,
                  List<ClinicalExamination> clinicalExaminations, List<VaccinationProcedure> vaccinationProcedures) {
        this(name, birthDay, petType, gender, breed, client);
        this.externalParasiteProcedures = externalParasiteProcedures;
        this.dewormings = dewormings;
        this.reproductions = reproductions;
        this.clinicalExaminations = clinicalExaminations;
        this.vaccinationProcedures = vaccinationProcedures;
    }

    public void addExternalParasiteProcedure(ExternalParasiteProcedure externalParasiteProcedure) {
        externalParasiteProcedures.add(externalParasiteProcedure);
        externalParasiteProcedure.setPet(this);
    }

    public void addVaccinationProcedure(VaccinationProcedure vaccinationProcedure) {
        vaccinationProcedures.add(vaccinationProcedure);
        vaccinationProcedure.setPet(this);
    }

    public void removeExternalParasiteProcedure(ExternalParasiteProcedure externalParasiteProcedure) {
        externalParasiteProcedures.remove(externalParasiteProcedure);
        externalParasiteProcedure.setPet(null);
    }

    public void removeVaccinationProcedure(VaccinationProcedure vaccinationProcedure) {
        vaccinationProcedures.remove(vaccinationProcedure);
        vaccinationProcedure.setPet(null);
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
