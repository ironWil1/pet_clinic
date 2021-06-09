package com.vet24.models.dto.medicine;

import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Doctor;

import java.util.Set;

public class TreatmentDto {
    private Long id;
    private Long petId;
    private Long doctorId;
    private Set<Procedure> procedureSet;
}
