package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.EchinococcusDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EchinococcusMapper extends DtoMapper<EchinococcusProcedure, EchinococcusDto>,
        EntityMapper<EchinococcusDto, EchinococcusProcedure> {

    @Mapping(source = "medicine.id", target = "medicineId")
    @Override
    EchinococcusDto toDto (EchinococcusProcedure echinococcusProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    @Override
    EchinococcusProcedure toEntity (EchinococcusDto echinococcusDto);

}
