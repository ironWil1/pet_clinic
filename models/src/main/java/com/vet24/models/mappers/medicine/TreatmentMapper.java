package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.TreatmentDto;
import com.vet24.models.medicine.Treatment;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class TreatmentMapper {

    public TreatmentDto treatmentToTreatmentDto(Treatment treatment){
        TreatmentDto treatmentDto = new TreatmentDto();
        treatmentDto.setProcedureSetId(treatment.getProcedureSet()
                .stream()
                .map(Procedure::getId)
                .collect(Collectors.toSet()));
        treatmentDto.setDoctorId(treatment.getDiagnosis().getDoctor().getId());
        treatmentDto.setId(treatment.getId());
        treatmentDto.setDiagnosisId(treatment.getDiagnosis().getId());
        return treatmentDto;
    }
}