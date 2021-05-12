package com.vet24.models.pet;

import lombok.*;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PetContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String ownerName;

    @NonNull
    private String address;

    @NonNull
    @Column(unique = true)
    private Long phone;

    @NonNull
    @Column(unique = true)
    private String uniqCode;

    @OneToOne(mappedBy = "petContact")
    private Pet pet;
}