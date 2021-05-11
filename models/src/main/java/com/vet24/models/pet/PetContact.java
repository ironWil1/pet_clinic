package com.vet24.models.pet;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PetContact {
    // связь с Pet через оне то оне
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 2L;
    private String ownerName = "Вася";
    private String address = "Луговое"; //определиться с наиболее подходящими типами данных
    private Long phone = 90887L;
    private Long uniqCode = 8398474987374826349L; // уникальный код для животного, генерируется на сервере при создании этой сущности.

    @OneToOne(mappedBy = "petContact")//(cascade = CascadeType.ALL)
    private Pet pet;

}