package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
import com.vet24.models.medicine.Diagnosis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PetMapper.class, ProcedureMapper.class})
public interface DiagnosisMapper {

    @Mapping(source="doctor.id", target="doctorId")
    DiagnosisDto diagnosisToDiagnosisDto(Diagnosis diagnosis);

}
