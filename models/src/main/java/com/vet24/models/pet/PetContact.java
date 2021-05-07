package com.vet24.models.pet;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class PetContact {
    // связь с Pet через оне то оне
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ownerName;
    private String address; //определиться с наиболее подходящими типами данных
    private Long phone;
    private Long uniqCode; // уникальный код для животного, генерируется на сервере при создании этой сущности.
}