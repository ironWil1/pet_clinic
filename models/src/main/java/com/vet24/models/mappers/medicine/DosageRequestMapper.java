package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.DosageRequestDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.medicine.Dosage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface DosageRequestMapper extends DtoMapper<Dosage, DosageRequestDto>, EntityMapper<DosageRequestDto,Dosage> {


}