package com.vet24.models.dto.pet.reproduction;

import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReproductionDto {
    @Null(groups = {OnCreate.class}, message = "Поле id должно быть null")
    @NotNull(groups = {OnUpdate.class}, message = "Поле id не должно быть null")
    Long id;
    LocalDate estrusStart;
    LocalDate mating;
    LocalDate dueDate;
    Integer childCount;
}
