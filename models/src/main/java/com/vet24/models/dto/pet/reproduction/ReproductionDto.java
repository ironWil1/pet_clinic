package com.vet24.models.dto.pet.reproduction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReproductionDto {
    Long id;
    LocalDate estrusStart;
    LocalDate mating;
    LocalDate dueDate;
    Integer childCount;
}
