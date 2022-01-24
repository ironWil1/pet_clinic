package com.vet24.models.dto.pet.reproduction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReproductionDto {
    @JsonView(View.Get.class)
    Long id;
    LocalDate estrusStart;
    LocalDate mating;
    LocalDate dueDate;
    Integer childCount;
}
