package com.vet24.models.medicine;

import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Doctor;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Treatment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Diagnosis diagnosis;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Procedure> procedures;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Doctor doctor;
}
