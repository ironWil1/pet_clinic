package com.vet24.models.dto.pet.clinicalexamination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicalExaminationDto {
    Long id;
    Double weight;
    Boolean isCanMove;
    String text;

}
