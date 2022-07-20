package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.DewormingDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.procedure.Deworming;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DewormingMapper extends DtoMapper<Deworming, DewormingDto>, EntityMapper<DewormingDto, Deworming> {

    @Mapping(source = "medicine.id", target = "medicineId")
    @Override
    DewormingDto toDto(Deworming deworming);

    @Mapping(source = "medicineId", target = "medicine.id")
    @Override
    Deworming toEntity(DewormingDto dewormingDto);

}
