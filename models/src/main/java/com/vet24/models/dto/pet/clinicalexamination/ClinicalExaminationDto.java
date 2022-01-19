package com.vet24.models.dto.pet.clinicalexamination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicalExaminationDto {
    @JsonIgnore
    Long id;
    Long petId;
    Double weight;
    Boolean isCanMove;
    String text;
}
