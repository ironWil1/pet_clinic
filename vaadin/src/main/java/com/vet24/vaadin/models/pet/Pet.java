package com.vet24.vaadin.models.pet;

import com.vet24.vaadin.models.enums.PetType;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Pet {

    private String name;

    private String avatar;

    private LocalDate birthDay;

    private PetType type;
}
