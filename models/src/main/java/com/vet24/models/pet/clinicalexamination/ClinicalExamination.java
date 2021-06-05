package com.vet24.models.pet.clinicalexamination;

import com.vet24.models.pet.Pet;
import com.vet24.models.user.Doctor;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"pet", "doctor"})
@Entity
public class ClinicalExamination implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @Column
    Integer weight; //вес, при том последний вес аписывается в таблицу животного

    @Column
    Boolean isCanMove; // животное здорово и выдержит перевозку до места назначения? true/false

    @Column
    String text; // общее описание состояния животного

    public ClinicalExamination(LocalDate date,
                               Pet pet,
                               Doctor doctor,
                               Integer weight,
                               Boolean isCanMove, String text) {
        this.date = date;
        this.pet = pet;
        this.doctor = doctor;
        this.weight = weight;
        this.isCanMove = isCanMove;
        this.text = text;
    }
}
