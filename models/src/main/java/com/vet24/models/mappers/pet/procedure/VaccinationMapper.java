package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VaccinationMapper extends DtoMapper<VaccinationProcedure, VaccinationDto>, EntityMapper<VaccinationDto, VaccinationProcedure> {

    @Mapping(source = "medicine.id", target = "medicineId")
    @Override
    VaccinationDto toDto(VaccinationProcedure vaccinationProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    @Override
    VaccinationProcedure toEntity(VaccinationDto vaccinationDto);

}
