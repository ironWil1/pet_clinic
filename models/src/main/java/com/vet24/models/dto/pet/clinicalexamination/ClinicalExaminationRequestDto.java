package com.vet24.models.dto.pet.clinicalexamination;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClinicalExaminationRequestDto {
    @Positive(message = "Поле weight должно быть больше 0")
    @NotNull(message = "Поле weight не может быть null")
    Double weight;
    Boolean isCanMove;
    String text;
}
