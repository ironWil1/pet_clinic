package com.vet24.models.dto.pet.clinicalexamination;

import com.vet24.models.user.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicalExaminationDto {
    Long id;
    Integer weight;
    Boolean isCanMove;
    String text;

}
