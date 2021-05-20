package com.vet24.models.pet;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PetContact {

    @Id
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
    private String petCode;

    @NonNull
    @Column(unique = true)
    private String email;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id")
    private Pet pet;
}