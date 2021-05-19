package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.EchinococcusDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EchinococcusMapper {

    @Mapping(source = "medicine.id", target = "medicineId")
    EchinococcusDto echinococcusToEchinococcusDto(EchinococcusProcedure echinococcusProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    EchinococcusProcedure echinococcusDtoToEchinococcus(EchinococcusDto echinococcusDto);

    @Mapping(source = "medicineId", target = "medicine.id")
    EchinococcusProcedure procedureDtoToEchinococcus(ProcedureDto procedureDto);
}
