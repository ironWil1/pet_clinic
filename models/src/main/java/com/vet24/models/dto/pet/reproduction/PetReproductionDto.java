package com.vet24.models.dto.pet.reproduction;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PetReproductionDto {
    private LocalDate estrusStart;
    private LocalDate mating;
    private LocalDate dueDate;
    private Integer childCount;
}
