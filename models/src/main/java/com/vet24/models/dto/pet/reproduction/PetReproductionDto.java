package com.vet24.models.dto.pet.reproduction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetReproductionDto {
    private LocalDate estrusStart;
    private LocalDate mating;
    private LocalDate dueDate;
    private Integer childCount;
}
