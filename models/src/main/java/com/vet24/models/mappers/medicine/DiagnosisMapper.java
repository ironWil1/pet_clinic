package com.vet24.models.mappers.medicine;
import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
import com.vet24.models.mappers.user.DoctorMapper;
import com.vet24.models.medicine.Diagnosis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PetMapper.class, DoctorMapper.class, ProcedureMapper.class})
public interface DiagnosisMapper {

    DiagnosisDto diagnosisToDiagnosisDto(Diagnosis diagnosis);

}
