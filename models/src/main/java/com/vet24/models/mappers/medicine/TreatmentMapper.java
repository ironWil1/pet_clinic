package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.TreatmentDto;
import com.vet24.models.medicine.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    @Mapping(source = "diagnosis.id", target = "diagnosisId")
    @Mapping(source = "diagnosis.doctor.id", target = "doctorId")
    @Mapping(source = "procedureList", target = "procedureDtoList")
    TreatmentDto treatmentToTreatmentDto(Treatment treatment);
}