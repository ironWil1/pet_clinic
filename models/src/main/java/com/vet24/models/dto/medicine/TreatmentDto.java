package com.vet24.models.dto.medicine;

import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Doctor;
import lombok.Data;

import java.util.Set;

@Data
public class TreatmentDto {
     Long id;
     Long diagnosisId;
     Set<Long> procedureSetId;
     Long doctorId;
}
