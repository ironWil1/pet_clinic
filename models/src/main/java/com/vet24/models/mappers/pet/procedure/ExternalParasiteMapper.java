package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExternalParasiteMapper {

    @Mapping(source = "medicine.id", target = "medicineId")
    ExternalParasiteDto externalParasiteToExternalParasiteDto(ExternalParasiteProcedure externalParasiteProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    ExternalParasiteProcedure externalParasiteDtoToExternalParasite(ExternalParasiteDto externalParasiteDto);

    @Mapping(source = "medicineId", target = "medicine.id")
    ExternalParasiteProcedure procedureDtoToExternalParasite(ProcedureDto procedureDto);
}
