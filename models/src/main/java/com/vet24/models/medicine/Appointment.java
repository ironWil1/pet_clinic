package com.vet24.models.medicine;

import com.vet24.models.pet.Pet;
import com.vet24.models.user.Doctor;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    private Pet pet;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private String description;

    public Appointment(Doctor doctor, Pet pet, LocalDateTime startDateTime, String description) {
        this.doctor = doctor;
        this.pet = pet;
        this.startDateTime = startDateTime;
        this.description = description;
    }
}
