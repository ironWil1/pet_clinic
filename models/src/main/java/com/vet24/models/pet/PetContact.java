package com.vet24.models.pet;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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