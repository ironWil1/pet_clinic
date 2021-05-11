package com.vet24.models.pet;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PetContact {
    // связь с Pet через оне то оне
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String ownerName;

    @NonNull
    private String address; //определиться с наиболее подходящими типами данных

    @NonNull
    private Long phone;

    @NonNull
    private Long uniqCode; // уникальный код для животного, генерируется на сервере при создании этой сущности.

    @OneToOne(mappedBy = "petContact")
    private Pet pet;
}