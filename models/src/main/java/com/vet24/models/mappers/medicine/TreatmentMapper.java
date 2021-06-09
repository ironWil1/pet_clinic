package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.TreatmentDto;
import com.vet24.models.medicine.Treatment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {
    Treatment treatmentDtoToTreatment(TreatmentDto treatmentDto);
    TreatmentDto treatmentToTreatmentDto(Treatment treatment);
}
