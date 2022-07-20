package com.vet24.models.dto.medicine;

import lombok.Data;

import java.util.List;

@Data
public class TreatmentDto {
     Long id;
     Long diagnosisId;
     Long doctorId;
}
