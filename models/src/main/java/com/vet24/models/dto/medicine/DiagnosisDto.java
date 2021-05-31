package com.vet24.models.dto.medicine;

import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.dto.user.DoctorDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DiagnosisDto {
    private Long id;
    private PetDto pet;
    private DoctorDto doctor;
    private String description;
    private TreatmentDto treatment;
}
