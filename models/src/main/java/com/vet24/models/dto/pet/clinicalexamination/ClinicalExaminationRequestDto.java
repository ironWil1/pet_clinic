package com.vet24.models.dto.pet.clinicalexamination;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClinicalExaminationRequestDto {
    Double weight;
    Boolean isCanMove;
    String text;
}
