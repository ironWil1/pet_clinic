package com.vet24.models.medicine;

import com.vet24.models.pet.Pet;
import com.vet24.models.user.Doctor;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
public class Diagnosis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected Doctor doctor;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected Pet pet;

    @NonNull
    @Column(nullable = false)
    private String description;

}
