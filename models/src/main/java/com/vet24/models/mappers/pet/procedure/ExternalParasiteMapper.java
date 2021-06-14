package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExternalParasiteMapper extends DtoMapper<ExternalParasiteProcedure, ExternalParasiteDto>,
        EntityMapper<ExternalParasiteDto, ExternalParasiteProcedure> {

    @Mapping(source = "medicine.id", target = "medicineId")
    @Override
    ExternalParasiteDto toDto (ExternalParasiteProcedure externalParasiteProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    @Override
    ExternalParasiteProcedure toEntity (ExternalParasiteDto externalParasiteDto);

}
