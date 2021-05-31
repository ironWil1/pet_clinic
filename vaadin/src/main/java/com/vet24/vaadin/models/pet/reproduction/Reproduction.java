package com.vet24.vaadin.models.pet.reproduction;

import com.vet24.vaadin.models.pet.Pet;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(exclude = "pet")
@NoArgsConstructor
@AllArgsConstructor
public class Reproduction implements Serializable {

    private Long id;

    private LocalDate estrusStart;

    private LocalDate mating;

    private LocalDate dueDate;

    private Integer childCount;

    private Pet pet;

    public Reproduction(LocalDate estrusStart, LocalDate mating, LocalDate dueDate, Integer childCount, Pet pet) {
        this.estrusStart = estrusStart;
        this.mating = mating;
        this.dueDate = dueDate;
        this.childCount = childCount;
        this.pet = pet;
    }
}
