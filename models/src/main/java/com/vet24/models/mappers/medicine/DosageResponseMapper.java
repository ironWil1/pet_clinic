package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.DosageResponseDto;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.medicine.Dosage;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DosageResponseMapper extends EntityMapper<DosageResponseDto, Dosage> {

}
