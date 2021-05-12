package com.vet24.models.pet;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String petName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private PetContact petContact;

}
