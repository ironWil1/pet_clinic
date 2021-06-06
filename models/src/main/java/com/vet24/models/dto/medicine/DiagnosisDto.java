package com.vet24.models.dto.medicine;

import com.vet24.models.dto.pet.PetDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DiagnosisDto {
    private Long id;
    private PetDto pet;
    private Long doctorId;
    private String description;
    private TreatmentDto treatment;
}
