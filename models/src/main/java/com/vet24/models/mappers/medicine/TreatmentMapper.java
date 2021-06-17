package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.TreatmentDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.medicine.Treatment;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    @Mapping(source = "diagnosis.id", target = "diagnosisId")
    @Mapping(source = "diagnosis.doctor.id", target = "doctorId")
    @Mapping(source = "procedureList", target = "procedureDtoList")
    TreatmentDto treatmentToTreatmentDto(Treatment treatment);

    default ProcedureDto toProcedureDto(Procedure procedure){
        return new ProcedureDto(procedure.getId(),
                procedure.getDate(),
                procedure.getType(),
                procedure.getMedicine().getId(),
                procedure.getMedicineBatchNumber(),
                procedure.getIsPeriodical(),
                procedure.getPeriodDays());
    }

}