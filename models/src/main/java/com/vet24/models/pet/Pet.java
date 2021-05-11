package com.vet24.models.pet;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Pet {

    @Id
    //@GeneratedValue(strategy = GenerationType.TABLE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String petName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)//, cascade = CascadeType.ALL
    @JoinTable(name = "pet_petcontact", joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "petcontact_id"))
    private PetContact petContact;

}
