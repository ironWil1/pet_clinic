package com.vet24.models.dto.medicine;

import com.vet24.models.dto.pet.procedure.ProcedureDto;
import lombok.Data;

import java.util.Set;

@Data
public class TreatmentDto {

    private Long id;
    private Set<ProcedureDto> procedures;
}
