package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExternalParasiteMapper {

    ExternalParasiteDto externalParasiteToExternalParasiteDto(ExternalParasiteProcedure externalParasiteProcedure);

    ExternalParasiteProcedure externalParasiteDtoToExternalParasite(ExternalParasiteDto externalParasiteDto);

    ExternalParasiteProcedure procedureDtoToExternalParasite(ProcedureDto procedureDto);
}
